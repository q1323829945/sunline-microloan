package cn.sunline.saas.consumer_loan.service

import cn.sunline.saas.consumer_loan.exception.LoanBusinessException
import cn.sunline.saas.consumer_loan.service.dto.*
import cn.sunline.saas.customer.offer.modules.ApplyStatus
import cn.sunline.saas.customer.offer.modules.dto.DTOCustomerOfferLoanView
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.customer_offer.service.dto.DTOCustomerOfferPage
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.LoanFeeType
import cn.sunline.saas.global.constant.UnderwritingType
import cn.sunline.saas.global.constant.YesOrNo
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstructionType
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.party.person.model.PersonIdentificationType
import cn.sunline.saas.party.person.service.PersonService
import cn.sunline.saas.repayment.instruction.service.RepaymentInstructionService
import cn.sunline.saas.rpc.invoke.CustomerOfferInvoke
import cn.sunline.saas.rpc.invoke.impl.PageInvokeImpl
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.aspectj.apache.bcel.generic.Instruction
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal
import javax.persistence.criteria.Predicate


@Service
class LoanBusinessManagerService(
    private val customerOfferInvoke: CustomerOfferInvoke,
    private val tenantDateTime: TenantDateTime
) {


    @Autowired
    private lateinit var personService: PersonService

    @Autowired
    private lateinit var customerOfferService: CustomerOfferService

    @Autowired
    private lateinit var customerLoanApplyService: CustomerLoanApplyService

    @Autowired
    private lateinit var repaymentInstructionService: RepaymentInstructionService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun getPaged(
        identificationNo: String,
        identificationType: PersonIdentificationType,
        pageable: Pageable
    ): Page<DTOLoanBusinessView> {

        val person = personService.findByIdentification(identificationNo, identificationType)
            ?: throw LoanBusinessException("Invalid Person", ManagementExceptionCode.DATA_NOT_FOUND)
        val customerOfferPaged = customerOfferService.getCustomerOfferPaged(person.id, null, null, pageable)

        return customerOfferPaged.map {
            val agreementViewInfo = it.id?.let { it1 -> customerOfferInvoke.getLoanAgreementInfo(it1) }
            var repaymentAmount = BigDecimal.ZERO
            if (agreementViewInfo?.id != null) {
                val repaymentInstruction = repaymentInstructionService.getPage(
                    agreementViewInfo.id.toLong(),
                    null,
                    MoneyTransferInstructionType.REPAYMENT,
                    InstructionLifecycleStatus.FULFILLED,
                    Pageable.unpaged()
                )
                if (repaymentInstruction.size > 0) {
                    repaymentAmount =
                        repaymentInstruction.sumOf { instruction -> instruction.moneyTransferInstructionAmount }
                }
            }
            DTOLoanBusinessView(
                agreementId = agreementViewInfo?.id,
                applicationId = it.id.toString(),
                disbursementAccount = agreementViewInfo?.disbursementAccount,
                loanProductType = agreementViewInfo?.loanProductType,
                currency = agreementViewInfo?.currency,
                loanAmount = agreementViewInfo?.amount,
                disbursementAmount = agreementViewInfo?.amount,
                repaymentAmount = repaymentAmount.toPlainString(),
                isSyndicatedLoan = YesOrNo.N,
                isRevolvingLoan = YesOrNo.N,
                status = agreementViewInfo?.agreementStatus
            )
        }
    }

    fun getLoanApplicationPaged(applicationId: String, pageable: Pageable): Page<DTOApplicationLoanView> {
        return Page.empty()
    }

    fun getFeeItemPaged(applicationId: String, pageable: Pageable): Page<DTOFeeItemView> {
        val loanAgreement = customerOfferInvoke.getLoanAgreement(applicationId.toLong())
            ?: throw LoanBusinessException("Invalid loan agreement", ManagementExceptionCode.DATA_NOT_FOUND)
        val dtoFeeItemViews = customerOfferInvoke.getFeeItemListByAgreementId(loanAgreement.id.toLong())
        val content = dtoFeeItemViews?.let { objectMapper.convertValue<List<DTOFeeItemView>>(it) }
        return if (content == null) Page.empty() else PageInvokeImpl<DTOFeeItemView>().rePaged(content, pageable)
    }

    fun getLoanDisbursementPaged(agreementId: String, pageable: Pageable): Page<DTOLoanDisbursementView> {
        val agreementViewInfo = customerOfferInvoke.getLoanAgreementInfoByAgreementId(agreementId.toLong())
            ?: throw LoanBusinessException("Invalid customer offer", ManagementExceptionCode.DATA_NOT_FOUND)
        val content = ArrayList<DTOLoanDisbursementView>()
        content.add(
            DTOLoanDisbursementView(
                agreementId = agreementId,
                applicationId = agreementViewInfo.applicationId,
                disbursementAccount = agreementViewInfo.disbursementAccount,
                disbursementAccountBank = agreementViewInfo.disbursementAccountBank,
                currency = agreementViewInfo.currency,
                fromDateTime = agreementViewInfo.fromDateTime,
                toDateTime = agreementViewInfo.toDateTime,
                signedDate = agreementViewInfo.signedDate,
                disbursementAmount = agreementViewInfo.amount,
            )
        )
        return PageInvokeImpl<DTOLoanDisbursementView>().rePaged(content, pageable)
    }

    fun getLoanHistoryEventPaged(agreementId: String, pageable: Pageable): Page<DTOLoanHistoryEventView> {
        val agreementViewInfo = customerOfferInvoke.getLoanAgreementInfoByAgreementId(agreementId.toLong())
            ?: throw LoanBusinessException("Invalid customer offer", ManagementExceptionCode.DATA_NOT_FOUND)

        return repaymentInstructionService.getPage(
            agreementId.toLong(),
            null,
            null,
            null,
            pageable
        ).map {
            DTOLoanHistoryEventView(
                agreementId = agreementId,
                eventName = it.moneyTransferInstructionType.name,
                eventDate = it.executeDateTime?.let { date -> tenantDateTime.toTenantDateTime(date) }.toString(),
                loanAmount = agreementViewInfo.amount,
                disbursementAmount = it.moneyTransferInstructionAmount.toPlainString(),
                repaidAmount = if (it.moneyTransferInstructionType == MoneyTransferInstructionType.DISBURSEMENT) BigDecimal.ZERO.toPlainString()
                else it.moneyTransferInstructionAmount.toPlainString()
            )
        }
    }

    fun getRepaymentRecordPaged(agreementId: String, pageable: Pageable): Page<DTORepaymentRecordView> {
        val page = repaymentInstructionService.getPage(
            agreementId.toLong(),
            null,
            MoneyTransferInstructionType.REPAYMENT,
            null,
            pageable
        )
        return page.map { record ->
            DTORepaymentRecordView(
                id = record.id.toString(),
                repaymentAmount = record.moneyTransferInstructionAmount.toPlainString(),
                currencyType = record.moneyTransferInstructionCurrency,
                status = record.moneyTransferInstructionStatus,
                payerAccount = record.payerAccount?.let { it }.toString(),
                agreementId = record.agreementId.toString(),
                userId = ContextUtil.getUserId(),
                customerId = record.businessUnit.toString(),
                referenceId = record.referenceId?.let { it }.toString(),
                startDateTime = record.startDateTime?.let { tenantDateTime.toTenantDateTime(it) }.toString(),
                endDateTime = record.endDateTime?.let { tenantDateTime.toTenantDateTime(it) }.toString(),
                executeDateTime = record.executeDateTime?.let { tenantDateTime.toTenantDateTime(it) }.toString()
            )
        }
    }

}