package cn.sunline.saas.consumer_loan.service

import cn.sunline.saas.consumer_loan.exception.RepaymentAgreementBusinessException
import cn.sunline.saas.consumer_loan.service.dto.DTOInvoicePage
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.customer_offer.exceptions.CustomerOfferStatusException
import cn.sunline.saas.document.template.services.LoanUploadConfigureService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.UnderwritingType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstructionType
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.repayment.instruction.service.RepaymentInstructionService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.rpc.invoke.CustomerOfferInvoke
import cn.sunline.saas.rpc.pubsub.LoanAgreementPublish
import cn.sunline.saas.underwriting.exception.UnderwritingNotFound
import cn.sunline.saas.underwriting.exception.UnderwritingStatusCannotBeUpdate
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import javax.persistence.criteria.Predicate


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
    ): Page<DTOInvoicePage> {

        val page = repaymentInstructionService.getPage(
            agreementId?.toLong(), customerId?.toLong(),
            MoneyTransferInstructionType.REPAYMENT, InstructionLifecycleStatus.PREPARED, pageable
        ).map {
            val invokesPaged =
                invoiceService.getInvokesPaged(it.businessUnit, InvoiceStatus.ACCOUNTED, pageable).first()
            val loanAgreement = customerOfferInvoke.getLoanAgreementInfo(it.agreementId)
            DTOInvoicePage(
                id = it.id.toString(),
                agreementId = it.agreementId.toString(),
                invoiceType = invokesPaged.invoiceType,
                fromDateTime = tenantDateTime.toTenantDateTime(invokesPaged.invoicePeriodFromDate).toString(),
                toDateTime = tenantDateTime.toTenantDateTime(invokesPaged.invoicePeriodToDate).toString(),
                invoiceStatus = invokesPaged.invoiceStatus,
                amount = invokesPaged.invoiceAmount.toPlainString(),
                currency = loanAgreement?.currency,
                userId = invokesPaged.invoicee.toString(),
                repaymentStatus = invokesPaged.repaymentStatus
            )
        }
        return page
    }

    fun getHistoryPaged(customerId: Long, pageable: Pageable): Page<DTOInvoicePage> {

        val page = invoiceService.getInvokesPaged(customerId, InvoiceStatus.FINISHED, pageable).map {

            val loanAgreement = customerOfferInvoke.getLoanAgreementInfo(it.agreementId)
            DTOInvoicePage(
                id = it.id.toString(),
                agreementId = it.agreementId.toString(),
                invoiceType = it.invoiceType,
                fromDateTime = tenantDateTime.toTenantDateTime(it.invoicePeriodFromDate).toString(),
                toDateTime = tenantDateTime.toTenantDateTime(it.invoicePeriodToDate).toString(),
                invoiceStatus = it.invoiceStatus,
                amount = it.invoiceAmount.toPlainString(),
                currency = loanAgreement?.currency,
                userId = it.invoicee.toString(),
                repaymentStatus = it.repaymentStatus
            )
        }
        return page
    }

//    fun finish(id: Long) {
//        updateStatus(id, InvoiceStatus.FINISHED)
//    }

//    fun rejected(id:Long){
//        updateStatus(id, ApplyStatus.REJECTED)
//    }

//    private fun updateStatus(id: Long, status: InvoiceStatus) {
//        val invoice = invoiceService.getOne(id) ?: throw RepaymentAgreementBusinessException(
//            "Loan Invoice Not Found",
//            ManagementExceptionCode.INVOICE_NOT_FOUND
//        )
//        checkStatus(invoice.invoiceStatus, status)
//        invoice.invoiceStatus = status
//        invoiceService.save(invoice)
//    }
//
//    private fun checkStatus(oldStatus: InvoiceStatus, newStatus: InvoiceStatus) {
//        when (oldStatus) {
//            InvoiceStatus.ACCOUNTED ->
//                if (newStatus != InvoiceStatus.FINISHED)
//                    throw CustomerOfferStatusException(
//                        "status can not be update",
//                        ManagementExceptionCode.INVOICE_STATUS_ERROR
//                    )
//            else -> throw RepaymentAgreementBusinessException(
//                "status can not be update",
//                ManagementExceptionCode.INVOICE_STATUS_ERROR
//            )
//        }
//    }

    fun finishLoanInvoiceRepayment(instructionId: String) {
        loanAgreementPublish.loanInvoiceRepaymentFinish(instructionId.toLong())
    }

    fun cancelLoanInvoiceRepayment(instructionId: String) {
        loanAgreementPublish.loanInvoiceRepaymentCancel(instructionId.toLong())
    }
}