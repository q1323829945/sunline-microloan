package cn.sunline.saas.consumer_loan.service

import cn.sunline.saas.consumer_loan.exception.RepaymentAgreementBusinessException
import cn.sunline.saas.consumer_loan.service.dto.DTOInvoiceLineView
import cn.sunline.saas.consumer_loan.service.dto.DTOInvoicePage
import cn.sunline.saas.consumer_loan.service.dto.DTOInvoiceTransferInstructionPage
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.customer_offer.exceptions.CustomerOfferStatusException
import cn.sunline.saas.document.template.services.LoanUploadConfigureService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.UnderwritingType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.InvoiceType
import cn.sunline.saas.invoice.model.RepaymentStatus
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
    ): Page<DTOInvoiceTransferInstructionPage> {

        val page = repaymentInstructionService.getPage(
            agreementId?.toLong(), customerId?.toLong(),
            MoneyTransferInstructionType.REPAYMENT, null, pageable
        ).map {
            val invokesPaged =
                invoiceService.getInvokesPaged(it.businessUnit, InvoiceStatus.ACCOUNTED, pageable).first()
            val loanAgreement = customerOfferInvoke.getLoanAgreementInfoByAgreementId(it.agreementId)
            DTOInvoiceTransferInstructionPage(
                id = it.id.toString(),
                invoiceId = invokesPaged.id.toString(),
                agreementId = it.agreementId.toString(),
                invoiceType = invokesPaged.invoiceType,
                invoicePeriodFromDate = tenantDateTime.toTenantDateTime(invokesPaged.invoicePeriodFromDate).toString(),
                invoicePeriodToDate = tenantDateTime.toTenantDateTime(invokesPaged.invoicePeriodToDate).toString(),
                invoiceRepaymentDate = tenantDateTime.toTenantDateTime(invokesPaged.invoiceRepaymentDate).toString(),
                invoiceStatus = invokesPaged.invoiceStatus,
                invoiceTotalAmount = invokesPaged.invoiceAmount.toPlainString(),
                invoiceCurrency = loanAgreement?.currency,
                invoicee = invokesPaged.invoicee.toString(),
                repaymentStatus = invokesPaged.repaymentStatus,
                instructionLifecycleStatus = it.moneyTransferInstructionStatus,
                loanAgreementFromDate = tenantDateTime.toTenantDateTime(invokesPaged.invoicePeriodFromDate).toString()
            )
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
        val page = Page.empty<DTOInvoiceLineView>(pageable)
        page.map {
            invoice?.let { invoice ->
                invoice.invoiceLines.forEach {
                    DTOInvoiceLineView(
                        Id = it.id.toString(),
                        invoiceId =invoiceId.toString(),
                        invoiceAmountType = it.invoiceAmountType,
                        invoiceAmount = it.invoiceAmount.toPlainString(),
                        repaymentAmount = it.repaymentAmount.toPlainString(),
                        repaymentStatus = it.repaymentStatus
                    )
                }
            }
        }
        return page
    }

    fun finishLoanInvoiceRepayment(instructionId: String) {
        loanAgreementPublish.loanInvoiceRepaymentFinish(instructionId.toLong())
    }

    fun cancelLoanInvoiceRepayment(instructionId: String) {
        loanAgreementPublish.loanInvoiceRepaymentCancel(instructionId.toLong())
    }
}