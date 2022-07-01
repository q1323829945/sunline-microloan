package cn.sunline.saas.consumer_loan.service

import cn.sunline.saas.consumer_loan.service.dto.DTOInvoiceLineView
import cn.sunline.saas.consumer_loan.service.dto.DTOInvoicePage
import cn.sunline.saas.consumer_loan.service.dto.DTOInvoiceTransferInstructionPage
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstructionType
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.repayment.instruction.service.RepaymentInstructionService
import cn.sunline.saas.rpc.invoke.CustomerOfferInvoke
import cn.sunline.saas.rpc.pubsub.LoanAgreementPublish
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.*
import org.springframework.stereotype.Service


@Service
class RepaymentAgreementManagerService(
    val tenantDateTime: TenantDateTime,
    val loanAgreementPublish: LoanAgreementPublish,
    val customerOfferInvoke: CustomerOfferInvoke
) {
    @Autowired
    private lateinit var customerOfferService: CustomerOfferService

    @Autowired
    private lateinit var invoiceService: InvoiceService

    @Autowired
    private lateinit var repaymentInstructionService: RepaymentInstructionService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getPaged(
        agreementId: String?,
        customerId: String?,
        moneyTransferInstructionType: MoneyTransferInstructionType,
        moneyTransferInstructionStatus: InstructionLifecycleStatus, pageable: Pageable
    ): Page<DTOInvoiceTransferInstructionPage> {

        val page = repaymentInstructionService.getPage(
            agreementId?.toLong(), customerId?.toLong(),
            MoneyTransferInstructionType.REPAYMENT, null, pageable
        ).map { record ->
            val invoice = record.referenceId?.let { invoiceId -> invoiceService.getOne(invoiceId)}
            invoice?.let{ inovice ->
                val loanAgreement = customerOfferInvoke.getLoanAgreementInfoByAgreementId(record.agreementId)
                DTOInvoiceTransferInstructionPage(
                    id = record.id.toString(),
                    invoiceId = invoice.id.toString(),
                    agreementId = record.agreementId.toString(),
                    invoiceRepaymentDate = tenantDateTime.toTenantDateTime(invoice.invoiceRepaymentDate).toString(),
                    invoiceTotalAmount = invoice.invoiceAmount.toPlainString(),
                    invoiceCurrency = loanAgreement?.currency,
                    invoicee = invoice.invoicee.toString(),
                    instructionLifecycleStatus = record.moneyTransferInstructionStatus,
                    startDateTime = record.startDateTime?.let { tenantDateTime.toTenantDateTime(it) }.toString(),
                    endDateTime = record.endDateTime?.let { tenantDateTime.toTenantDateTime(it) }.toString(),
                    executeDateTime = record.executeDateTime?.let { tenantDateTime.toTenantDateTime(it) }.toString(),
                    operator = record.operator,
                )
            }
        }
        return page
    }

    fun getHistoryPaged(customerId: Long, pageable: Pageable): Page<DTOInvoicePage> {

        val page = invoiceService.getInvokesPaged(customerId, InvoiceStatus.FINISHED, pageable).map {

            val loanAgreement = customerOfferInvoke.getLoanAgreementInfoByAgreementId(it.agreementId)
            DTOInvoicePage(
                invoiceId = it.id.toString(),
                agreementId = it.agreementId.toString(),
                invoiceType = it.invoiceType,
                invoicePeriodFromDate = tenantDateTime.toTenantDateTime(it.invoicePeriodFromDate).toString(),
                invoicePeriodToDate = tenantDateTime.toTenantDateTime(it.invoicePeriodToDate).toString(),
                invoiceRepaymentDate = tenantDateTime.toTenantDateTime(it.invoiceRepaymentDate).toString(),
                invoiceStatus = it.invoiceStatus,
                invoiceTotalAmount = it.invoiceAmount.toPlainString(),
                invoiceCurrency = loanAgreement?.currency,
                invoicee = it.invoicee.toString(),
                repaymentStatus = it.repaymentStatus
            )
        }
        return page
    }

    fun getInvoiceDetail(invoiceId: Long, pageable: Pageable): Page<DTOInvoiceLineView> {
        val invoice = invoiceService.getOne(invoiceId)
        val list = ArrayList<DTOInvoiceLineView>()
        invoice?.let { invoice ->
            invoice.invoiceLines.forEach {
                list.add(
                    DTOInvoiceLineView(
                        Id = it.id.toString(),
                        invoiceId = invoiceId.toString(),
                        invoiceAmountType = it.invoiceAmountType,
                        invoiceAmount = it.invoiceAmount.toPlainString(),
                        repaymentAmount = it.repaymentAmount.toPlainString(),
                        repaymentStatus = it.repaymentStatus
                    )
                )
            }
        }
        return PageImpl(list)
    }

    fun fulfillLoanInvoiceRepayment(instructionId: String) {
        loanAgreementPublish.loanInvoiceRepaymentFulfill(instructionId.toLong())
    }

    fun failLoanInvoiceRepayment(instructionId: String) {
        loanAgreementPublish.loanInvoiceRepaymentFail(instructionId.toLong())
    }
}