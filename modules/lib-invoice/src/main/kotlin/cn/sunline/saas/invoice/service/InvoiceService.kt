package cn.sunline.saas.invoice.service

import cn.sunline.saas.invoice.factory.InvoiceFactory
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.db.Invoice
import cn.sunline.saas.invoice.model.dto.DTOLoanInvoice
import cn.sunline.saas.invoice.model.RepaymentStatus
import cn.sunline.saas.invoice.repository.InvoiceRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Root

/**
 * @title: InvoiceService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/25 16:28
 */
@Service
class InvoiceService(private val invoiceRepository: InvoiceRepository) :
    BaseMultiTenantRepoService<Invoice, Long>(invoiceRepository) {

    @Autowired
    private lateinit var invoiceFactory: InvoiceFactory

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    fun initiateLoanInvoice(dtoLoanInvoice: DTOLoanInvoice): Invoice {
        invoiceFactory.getLoanInvoiceInstance(dtoLoanInvoice)

        return save(
            invoiceFactory.getLoanInvoiceInstance(dtoLoanInvoice)
        )
    }

    fun initiateLoanInvoice(dtoLoanInvoices: MutableList<DTOLoanInvoice>): Iterable<Invoice> {
        val invoices = mutableListOf<Invoice>()
        dtoLoanInvoices.forEach {
            invoices.add(invoiceFactory.getLoanInvoiceInstance(it))
        }
        return save(invoices)
    }

    fun listAccountedInvoices(agreementId: Long, pageable: Pageable): Page<Invoice> {
        val agreementIdSpecification: Specification<Invoice> =
            Specification { root: Root<Invoice>, _, criteriaBuilder ->
                val path: Expression<Long> = root.get("agreementId")
                val predicate = criteriaBuilder.equal(path, agreementId)
                criteriaBuilder.and(predicate)
            }

        val invoiceStatusSpecification: Specification<Invoice> =
            Specification { root: Root<Invoice>, _, criteriaBuilder ->
                val path: Expression<Long> = root.get("invoiceStatus")
                val predicate = criteriaBuilder.equal(path, InvoiceStatus.ACCOUNTED)
                criteriaBuilder.and(predicate)
            }

        return getPageWithTenant(agreementIdSpecification.and(invoiceStatusSpecification), pageable)
    }

    fun listInvoiceByAgreementId(agreementId: Long, pageable: Pageable): Page<Invoice> {
        val agreementIdSpecification: Specification<Invoice> =
            Specification { root: Root<Invoice>, _, criteriaBuilder ->
                val path: Expression<Long> = root.get("agreementId")
                val predicate = criteriaBuilder.equal(path, agreementId)
                criteriaBuilder.and(predicate)
            }

        return getPageWithTenant(agreementIdSpecification, pageable)
    }

    fun calculateRepaymentAmountById(invoiceId: Long): BigDecimal {
        val invoice = getOne(invoiceId)
        return calculateRepaymentAmount(invoice)
    }

    fun calculateRepaymentAmount(invoice: Invoice?): BigDecimal {
        var repaymentAmount = BigDecimal.ZERO
        invoice?.invoiceLines?.forEach {
            repaymentAmount.add(it.invoiceAmount.subtract(it.repaymentAmount))
        }
        return repaymentAmount
    }

    fun repayInvoiceById(amount: BigDecimal, invoiceId: Long, graceDays: Int, today: DateTime) {
        val invoice = getOne(invoiceId)
        return repayInvoice(amount, invoice, graceDays, today)
    }

    fun repayInvoice(amount: BigDecimal, invoice: Invoice?, graceDays: Int, today: DateTime) {
        var balanceAmount = amount
        if (invoice != null) {
            invoice.invoiceLines.sortBy { it.invoiceAmountType.order }
            invoice.invoiceLines.forEach {
                val repaymentAmount = it.invoiceAmount.subtract(it.repaymentAmount)

                if (balanceAmount.subtract(repaymentAmount) >= BigDecimal.ZERO) {
                    it.repaymentAmount = it.invoiceAmount
                    balanceAmount = balanceAmount.subtract(repaymentAmount)
                    it.repaymentStatus = RepaymentStatus.CLEAR
                } else {
                    it.repaymentAmount = it.repaymentAmount.add(balanceAmount)
                    balanceAmount = BigDecimal.ZERO
                    it.repaymentStatus = RepaymentStatus.OPEN
                }
            }
            invoice.repaymentAmount = invoice.repaymentAmount.add(amount)

            if (invoice.invoiceLines.none { it.repaymentStatus != RepaymentStatus.CLEAR }) {
                invoice.repaymentStatus = RepaymentStatus.CLEAR
            } else {
                if (determineOverdueStatus(
                        tenantDateTime.toTenantDateTime(invoice.invoiceRepaymentDate), graceDays, today
                    )
                ) {
                    invoice.repaymentStatus = RepaymentStatus.OVERDUE
                } else {
                    invoice.repaymentStatus = RepaymentStatus.OPEN
                }
            }

            if (invoice.repaymentStatus == RepaymentStatus.CLEAR) {
                invoice.invoiceStatus = InvoiceStatus.FINISHED
            }

            save(invoice)
        }
    }

    fun determineOverdueStatus(repaymentDate: DateTime, graceDays: Int, now: DateTime): Boolean {
        return tenantDateTime.getYearMonthDay(repaymentDate.plusDays(graceDays)) < tenantDateTime.getYearMonthDay(now)
    }

}