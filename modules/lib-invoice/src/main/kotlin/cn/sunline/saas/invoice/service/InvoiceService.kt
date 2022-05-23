package cn.sunline.saas.invoice.service

import cn.sunline.saas.invoice.factory.InvoiceFactory
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.InvoiceType
import cn.sunline.saas.invoice.model.db.Invoice
import cn.sunline.saas.invoice.model.db.InvoiceLine
import cn.sunline.saas.invoice.model.dto.DTOLoanInvoice
import cn.sunline.saas.invoice.repository.InvoiceRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import org.joda.time.Instant

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
        invoiceFactory.getInstance(dtoLoanInvoice)

        return save(
            invoiceFactory.getInstance(dtoLoanInvoice)
        )
    }

    fun initiateLoanInvoice(dtoLoanInvoices: MutableList<DTOLoanInvoice>): Iterable<Invoice> {
        val invoices = mutableListOf<Invoice>()
        dtoLoanInvoices.forEach {
            invoices.add(invoiceFactory.getInstance(it))
        }
        return save(invoices)
    }

}