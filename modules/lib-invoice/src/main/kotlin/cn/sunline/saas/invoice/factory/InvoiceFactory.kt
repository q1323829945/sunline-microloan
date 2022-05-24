package cn.sunline.saas.invoice.factory

import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.InvoiceType
import cn.sunline.saas.invoice.model.db.Invoice
import cn.sunline.saas.invoice.model.db.InvoiceLine
import cn.sunline.saas.invoice.model.dto.DTOLoanInvoice
import cn.sunline.saas.seq.Sequence
import org.joda.time.Instant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal

/**
 * @title: InvoiceFactory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/23 13:43
 */
@Component
class InvoiceFactory {
    @Autowired
    private lateinit var seq: Sequence

    fun getInstance(dtoLoanInvoice: DTOLoanInvoice): Invoice {
        val invoiceLine = mutableListOf<InvoiceLine>()

        var invoiceAmount: BigDecimal = BigDecimal.ZERO

        invoiceLine += InvoiceLine(
            id = seq.nextId(),
            invoiceAmountType = InvoiceAmountType.PRINCIPAL,
            invoiceAmount = dtoLoanInvoice.principal
        )
        invoiceAmount = invoiceAmount.add(dtoLoanInvoice.principal)

        invoiceLine += InvoiceLine(
            id = seq.nextId(),
            invoiceAmountType = InvoiceAmountType.INTEREST,
            invoiceAmount = dtoLoanInvoice.interest
        )
        invoiceAmount = invoiceAmount.add(dtoLoanInvoice.interest)

        dtoLoanInvoice.fee?.run {
            invoiceLine += InvoiceLine(
                id = seq.nextId(),
                invoiceAmountType = InvoiceAmountType.FEE,
                invoiceAmount = this
            )
            invoiceAmount = invoiceAmount.add(this)
        }

        //TODO create invoice document
        val document: String = ""

        return Invoice(
            id = seq.nextId(),
            invoiceType = InvoiceType.LOAN,
            invoiceDueDate = Instant(dtoLoanInvoice.invoicePeriodToDate),
            invoicePeriodFromDate = Instant(dtoLoanInvoice.invoicePeriodFromDate),
            invoicePeriodToDate = Instant(dtoLoanInvoice.invoicePeriodToDate),
            invoiceAssignedDocument = document,
            invoiceAmount = invoiceAmount,
            invoiceStatus = InvoiceStatus.INITIATE,
            invoicee = dtoLoanInvoice.invoicee,
            invoiceLines = invoiceLine,
            agreementId = dtoLoanInvoice.agreementId
        )
    }
}