package cn.sunline.saas.consumer_loan.service

import cn.sunline.saas.consumer_loan.service.dto.*
import cn.sunline.saas.customer.offer.services.CustomerLoanApplyService
import cn.sunline.saas.customer.offer.services.CustomerOfferService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.fee.arrangement.component.FeeArrangementHelper
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementView
import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.YesOrNo
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.interest.component.InterestRateHelper
import cn.sunline.saas.interest.exception.InterestFeatureNotFoundException
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.loan.agreement.exception.LoanAgreementNotFoundException
import cn.sunline.saas.loan.product.exception.LoanProductNotFoundException
import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstructionType
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.party.person.exception.PersonNotFoundException
import cn.sunline.saas.party.person.model.PersonIdentificationType
import cn.sunline.saas.party.person.service.PersonService
import cn.sunline.saas.repayment.instruction.service.RepaymentInstructionService
import cn.sunline.saas.rpc.invoke.CustomerOfferInvoke
import cn.sunline.saas.rpc.invoke.impl.PageInvokeImpl
import cn.sunline.saas.rpc.invoke.impl.ProductInvokeImpl
import cn.sunline.saas.schedule.ScheduleService
import cn.sunline.saas.schedule.util.ScheduleHelper
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal


@Service
class LoanBusinessManagerService(
    private val customerOfferInvoke: CustomerOfferInvoke,
    private val tenantDateTime: TenantDateTime,

    ) {

    @Autowired
    private lateinit var productInvokeImpl: ProductInvokeImpl

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
            ?: throw PersonNotFoundException("Invalid Person")
        val filter = customerOfferService.getCustomerOfferPaged(person.id, null, null, pageable).content
            .filter { it.status != ApplyStatus.RECORD && it.status != ApplyStatus.SUBMIT }

        return customerOfferService.rePaged(filter, pageable).map {
            var repaymentAmount = BigDecimal.ZERO
            val agreementViewInfo = it.id?.let { it1 -> customerOfferInvoke.getLoanAgreementInfo(it1) }
            if (agreementViewInfo == null) {
                DTOLoanBusinessView(
                    applicationId = it.id.toString(),
                    repaymentAmount = repaymentAmount.toPlainString(),
                    syndicatedLoan = YesOrNo.N,
                    revolvingLoan = YesOrNo.N
                )
            } else {
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
                DTOLoanBusinessView(
                    agreementId = agreementViewInfo.id,
                    applicationId = it.id.toString(),
                    disbursementAccount = agreementViewInfo.disbursementAccount,
                    loanProductType = agreementViewInfo.loanProductType,
                    currency = agreementViewInfo.currency,
                    loanAmount = agreementViewInfo.amount,
                    disbursementAmount = agreementViewInfo.amount,
                    repaymentAmount = repaymentAmount.toPlainString(),
                    syndicatedLoan = YesOrNo.N,
                    revolvingLoan = YesOrNo.N,
                    status = agreementViewInfo.agreementStatus
                )
            }
        }
    }

    fun getLoanApplicationPaged(applicationId: String, pageable: Pageable): Page<DTOApplicationLoanView> {
        return Page.empty()
    }

    fun getFeeItemPaged(applicationId: String, pageable: Pageable): Page<DTOFeeItemView> {
        val loanAgreement = customerOfferInvoke.getLoanAgreement(applicationId.toLong())
            ?: throw LoanAgreementNotFoundException("Invalid loan agreement")
        val dtoFeeItemViews = customerOfferInvoke.getFeeItemListByAgreementId(loanAgreement.id.toLong())
        val content = ArrayList<DTOFeeItemView>()
        dtoFeeItemViews?.let { it ->
            it.forEach {
                content.add(
                    DTOFeeItemView(
                        agreementId = loanAgreement.id,
                        applicationId = applicationId,
                        loanFeeType = it.loanFeeType,
                        loanFeeTypeName = it.loanFeeTypeName,
                        currency = it.currency,
                        feeAmountOrRatio = it.feeAmountOrRatio,
                        nonPaymentAmount = it.nonPaymentAmount
                    )
                )
            }
        }
        return PageInvokeImpl<DTOFeeItemView>().rePaged(content, pageable)
    }

    fun getLoanDisbursementPaged(agreementId: String, pageable: Pageable): Page<DTOLoanDisbursementView> {
        val agreementViewInfo = customerOfferInvoke.getLoanAgreementInfoByAgreementId(agreementId.toLong())
            ?: throw LoanAgreementNotFoundException("Invalid customer offer")
        val content = ArrayList<DTOLoanDisbursementView>()
        content.add(
            DTOLoanDisbursementView(
                agreementId = agreementId,
                applicationId = agreementViewInfo.applicationId,
                disbursementAccount = agreementViewInfo.disbursementAccount,
                disbursementAccountBank = agreementViewInfo.disbursementAccountBank,
                currency = agreementViewInfo.currency,
                fromDateTime = tenantDateTime.toTenantDateTime(agreementViewInfo.fromDateTime).toString(),
                toDateTime = agreementViewInfo.toDateTime,
                signedDate = agreementViewInfo.signedDate,
                disbursementAmount = agreementViewInfo.amount,
            )
        )
        return PageInvokeImpl<DTOLoanDisbursementView>().rePaged(content, pageable)
    }

    fun getLoanHistoryEventPaged(agreementId: String, pageable: Pageable): Page<DTOLoanHistoryEventView> {
        val agreementViewInfo = customerOfferInvoke.getLoanAgreementInfoByAgreementId(agreementId.toLong())
            ?: throw LoanAgreementNotFoundException("Invalid customer offer")

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

    fun calculateTrailSchedule(
        productId: String,
        amount: String,
        term: LoanTermType
    ): ScheduleHelper.DTORepaymentScheduleTrialView {

        val loanProduct = customerOfferInvoke.getProduct(productId.toLong())
            ?: throw LoanProductNotFoundException("Invalid loan product")

        val feeArrangement = loanProduct.feeFeatures?.run {
            objectMapper.convertValue<MutableList<DTOFeeArrangementView>>(
                this
            )
        }

        val feeDeductItem = FeeArrangementHelper.getDisbursementFeeDeductItem(feeArrangement, amount.toBigDecimal())

        val interestFeature = loanProduct.interestFeature ?: throw InterestFeatureNotFoundException("Invalid interest feature")

        val rateResult =
            productInvokeImpl.getRatePlanWithInterestRate(interestFeature.ratePlanId.toLong())
        val rates = rateResult.rates.map { objectMapper.convertValue<InterestRate>(it) }.toMutableList()
        val interestRate =
            InterestRateHelper.getExecutionRate(
                interestFeature.interestType,
                term,
                amount.toBigDecimal(),
                interestFeature.interest.floatPoint,
                interestFeature.interest.floatRatio,
                rates
            )
        val schedule = ScheduleService(
            amount.toBigDecimal(),
            interestRate,
            term,
            loanProduct.repaymentFeature?.payment?.frequency!!,
            loanProduct.repaymentFeature?.payment?.repaymentDayType,
            loanProduct.interestFeature?.interest?.baseYearDays!!,
            tenantDateTime.now(),
            null,
            null,
            feeDeductItem.scheduleFee
        ).getSchedules(loanProduct.repaymentFeature?.payment?.paymentMethod!!)
        return ScheduleHelper.convertToScheduleTrialMapper(schedule, feeDeductItem.immediateFee)
    }

}