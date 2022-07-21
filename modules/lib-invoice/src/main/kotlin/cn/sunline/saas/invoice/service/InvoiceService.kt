package cn.sunline.saas.invoice.service

import cn.sunline.saas.invoice.factory.InvoiceFactory
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.db.Invoice
import cn.sunline.saas.invoice.model.dto.DTOLoanInvoice
import cn.sunline.saas.global.constant.RepaymentStatus
import cn.sunline.saas.invoice.repository.InvoiceRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import java.math.BigDecimal
import javax.persistence.criteria.Expression
import javax.persistence.criteria.Predicate
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

    fun initiateLoanInvoice(dtoLoanInvoices: MutableList<DTOLoanInvoice>): Iterable<Invoice> {
        val invoices = mutableListOf<Invoice>()
        dtoLoanInvoices.forEach {
            invoices.add(invoiceFactory.getLoanInvoiceInstance(it))
        }
        return save(invoices)
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
            repaymentAmount = repaymentAmount.add(it.invoiceAmount.subtract(it.repaymentAmount))
        }
        return repaymentAmount
    }

    fun repayInvoice(amount: BigDecimal, invoice: Invoice?, graceDays: Int, today: DateTime):MutableMap<InvoiceAmountType,BigDecimal> {
        val repaymentItem = mutableMapOf<InvoiceAmountType,BigDecimal>()
        var balanceAmount = amount
        if (invoice != null) {
            invoice.invoiceLines.sortBy { it.invoiceAmountType.order }
            invoice.invoiceLines.forEach {
                val repaymentAmount = it.invoiceAmount.subtract(it.repaymentAmount)

                if (balanceAmount.subtract(repaymentAmount) >= BigDecimal.ZERO) {
                    it.repaymentAmount = it.invoiceAmount
                    balanceAmount = balanceAmount.subtract(repaymentAmount)
                    it.repaymentStatus = RepaymentStatus.CLEAR

                    repaymentItem[it.invoiceAmountType] = repaymentAmount
                } else {
                    it.repaymentAmount = it.repaymentAmount.add(balanceAmount)
                    balanceAmount = BigDecimal.ZERO
                    it.repaymentStatus = RepaymentStatus.OPEN

                    repaymentItem[it.invoiceAmountType] = balanceAmount
                }
            }
            invoice.repaymentAmount = invoice.repaymentAmount.add(amount)

            if (invoice.invoiceLines.none { it.repaymentStatus != RepaymentStatus.CLEAR }) {
                invoice.repaymentStatus = RepaymentStatus.CLEAR
                invoice.invoiceStatus = InvoiceStatus.FINISHED
            } else {
                invoice.repaymentStatus = RepaymentStatus.OPEN
            }

            save(invoice)
        }
        return repaymentItem
    }

    fun overdueInvoice(invoices:MutableList<Invoice>, graceDays: Int, now: DateTime): BigDecimal {
        var overduePrincipal = BigDecimal.ZERO
        invoices.forEach {
            val repaymentDate = tenantDateTime.toTenantDateTime(it.invoiceRepaymentDate)
            if (tenantDateTime.getYearMonthDay(repaymentDate.plusDays(graceDays)) < tenantDateTime.getYearMonthDay(now)){
                it.repaymentStatus = RepaymentStatus.OVERDUE
                val invoiceLine = it.invoiceLines.first { line -> line.invoiceAmountType == InvoiceAmountType.PRINCIPAL }
                overduePrincipal = overduePrincipal.add(invoiceLine.invoiceAmount.subtract(invoiceLine.repaymentAmount))
            }
        }
        return overduePrincipal
    }

    fun listInvoiceByInvoiceeAndDate(
        invoicee: Long,
        invoiceStartDate: String?,
        invoiceEndDate: String?
    ): Page<Invoice> {
        val specification: Specification<Invoice> =
            Specification { root: Root<Invoice>, _, criteriaBuilder ->
                val predicates = mutableListOf<Predicate>()
                predicates.add(criteriaBuilder.equal(root.get<InvoiceStatus>("invoiceStatus"), InvoiceStatus.FINISHED))
                invoicee.run { predicates.add(criteriaBuilder.equal(root.get<Long>("invoicee"), invoicee)) }
                invoiceStartDate?.run {
                    predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                            root.get("invoicePeriodFromDate"),
                            tenantDateTime.toTenantDateTime(invoiceStartDate).toDate()
                        )
                    )
                }
                invoiceEndDate?.run {
                    predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                            root.get("invoicePeriodToDate"),
                            tenantDateTime.toTenantDateTime(invoiceEndDate).toDate()
                        )
                    )
                }
                criteriaBuilder.and(*(predicates.toTypedArray()))
            }
        return getPageWithTenant(
            specification,
            Pageable.unpaged()
            //PageRequest.of(pageable.pageNumber, pageable.pageSize, Sort.by(Sort.Order.desc("invoicePeriodFromDate")))
        )
    }

    fun retrieveCurrentInvoices(
        invoicee: Long,
        invoiceStatus: InvoiceStatus?
    ): Page<Invoice> {
        val specification: Specification<Invoice> =
            Specification { root: Root<Invoice>, _, criteriaBuilder ->
                val predicates = mutableListOf<Predicate>()
                invoicee.run { predicates.add(criteriaBuilder.equal(root.get<Long>("invoicee"), invoicee)) }
                invoiceStatus?.run { predicates.add(criteriaBuilder.equal(root.get<Long>("invoiceStatus"), invoiceStatus)) }
                criteriaBuilder.and(*(predicates.toTypedArray()))
            }
        return getPageWithTenant(specification, Pageable.unpaged())
    }


    fun getInvokesPaged(customerId:Long?,invoiceStatus: InvoiceStatus?,pageable: Pageable): Page<Invoice>{
        val page = getPageWithTenant({root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            customerId?.run { predicates.add(criteriaBuilder.equal(root.get<Long>("invoicee"),customerId)) }
            invoiceStatus?.run { predicates.add(criteriaBuilder.equal(root.get<Long>("invoiceStatus"), invoiceStatus)) }
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },PageRequest.of(pageable.pageNumber,pageable.pageSize, Sort.by(Sort.Order.desc("invoiceRepaymentDate"))))
        return page
    }

}
