package cn.sunline.saas.customer.billing.service

import cn.sunline.saas.customer.billing.model.InvoiceAmountType
import cn.sunline.saas.customer.billing.model.InvoiceStatus
import cn.sunline.saas.customer.billing.model.InvoiceType
import cn.sunline.saas.customer.billing.model.db.Invoice
import cn.sunline.saas.customer.billing.model.db.InvoiceLine
import cn.sunline.saas.customer.billing.model.dto.DTOLoanInvoice
import cn.sunline.saas.customer.billing.repository.InvoiceRepository
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
    private lateinit var seq: Sequence

    fun initiateLoanInvoice(dtoLoanInvoice: DTOLoanInvoice): Invoice {
        val invoiceId = seq.nextId()

        val invoiceLine = mutableListOf<InvoiceLine>()

        invoiceLine.add(
            InvoiceLine(
                id = seq.nextId(),
                invoiceAmountType = InvoiceAmountType.PRINCIPAL,
                invoiceAmount = dtoLoanInvoice.principal
            )
        )
        invoiceLine.add(
            InvoiceLine(
                id = seq.nextId(),
                invoiceAmountType = InvoiceAmountType.INTEREST,
                invoiceAmount = dtoLoanInvoice.interest
            )
        )
        invoiceLine.add(
            InvoiceLine(
                id = seq.nextId(),
                invoiceAmountType = InvoiceAmountType.FEE,
                invoiceAmount = dtoLoanInvoice.fee
            )
        )

        val invoiceAmount = dtoLoanInvoice.principal.add(dtoLoanInvoice.interest).add(dtoLoanInvoice.fee)

        //TODO create invoice document
        val document: String = ""

        return save(
            Invoice(
                id = seq.nextId(),
                invoiceType = InvoiceType.LOAN,
                invoiceDueDate = Instant(dtoLoanInvoice.invoicePeriodToDate),
                invoicePeriodFromDate = Instant(dtoLoanInvoice.invoicePeriodFromDate),
                invoicePeriodToDate = Instant(dtoLoanInvoice.invoicePeriodToDate),
                invoiceAssignedDocument = document,
                invoiceAddress = dtoLoanInvoice.invoiceAddress,
                invoiceAmount = invoiceAmount,
                invoiceStatus = InvoiceStatus.PREPARE,
                invoicee = dtoLoanInvoice.invoicee,
                invoiceLines = invoiceLine
            )
        )
    }
}