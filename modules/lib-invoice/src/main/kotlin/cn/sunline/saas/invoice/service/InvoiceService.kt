package cn.sunline.saas.invoice.service

import cn.sunline.saas.invoice.factory.InvoiceFactory
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.db.Invoice
import cn.sunline.saas.invoice.model.dto.DTOLoanInvoice
import cn.sunline.saas.invoice.repository.InvoiceRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
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

    fun listInvoicesByAccounted(agreementId:Long,pageable: Pageable): Page<Invoice> {
        val agreementIdSpecification: Specification<Invoice> = Specification { root: Root<Invoice>, _, criteriaBuilder ->
            val path: Expression<Long> = root.get("agreementId")
            val predicate = criteriaBuilder.equal(path, agreementId)
            criteriaBuilder.and(predicate)
        }

        val invoiceStatusSpecification: Specification<Invoice> = Specification { root: Root<Invoice>, _, criteriaBuilder ->
            val path: Expression<Long> = root.get("invoiceStatus")
            val predicate = criteriaBuilder.equal(path, InvoiceStatus.ACCOUNTED)
            criteriaBuilder.and(predicate)
        }

        return getPageWithTenant(agreementIdSpecification.and(invoiceStatusSpecification),pageable)
    }

}