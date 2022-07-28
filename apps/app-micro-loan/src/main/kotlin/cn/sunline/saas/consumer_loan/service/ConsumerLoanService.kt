package cn.sunline.saas.consumer_loan.service

import cn.sunline.saas.account.exception.AccountNotFoundException
import cn.sunline.saas.account.model.AccountBalanceType
import cn.sunline.saas.account.service.LoanAccountService
import cn.sunline.saas.banking.transaction.model.dto.DTOBankingTransaction
import cn.sunline.saas.consumer_loan.event.ConsumerLoanPublish
import cn.sunline.saas.consumer_loan.exception.DisbursementArrangementNotFoundException
import cn.sunline.saas.consumer_loan.exception.LoanAgreementStatusCheckException
import cn.sunline.saas.consumer_loan.exception.RepaymentAgreementBusinessException
import cn.sunline.saas.consumer_loan.invoke.ConsumerLoanInvoke
import cn.sunline.saas.consumer_loan.service.assembly.ConsumerLoanAssembly
import cn.sunline.saas.consumer_loan.service.dto.*
import cn.sunline.saas.disbursement.arrangement.service.DisbursementArrangementService
import cn.sunline.saas.disbursement.instruction.model.dto.DTODisbursementInstructionAdd
import cn.sunline.saas.disbursement.instruction.service.DisbursementInstructionService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.fee.arrangement.model.db.FeeArrangement
import cn.sunline.saas.fee.arrangement.model.db.FeeItem
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementAdd
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementView
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeItemAdd
import cn.sunline.saas.fee.arrangement.service.FeeArrangementService
import cn.sunline.saas.fee.arrangement.service.FeeItemService
import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.fee.util.FeeUtil
import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.formula.constant.CalculatePrecision
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.interest.arrangement.component.getExecutionRate
import cn.sunline.saas.interest.arrangement.exception.BaseRateNullException
import cn.sunline.saas.interest.component.InterestRateHelper
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.invoice.arrangement.exception.InvoiceArrangementNotFoundException
import cn.sunline.saas.invoice.arrangement.service.InvoiceArrangementService
import cn.sunline.saas.invoice.exception.InvoiceNotFoundException
import cn.sunline.saas.invoice.exception.LoanInvoiceBusinessException
import cn.sunline.saas.invoice.model.InvoiceAmountType
import cn.sunline.saas.invoice.model.InvoiceStatus
import cn.sunline.saas.invoice.model.db.Invoice
import cn.sunline.saas.invoice.model.dto.*
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.loan.agreement.exception.LoanAgreementNotFoundException
import cn.sunline.saas.loan.agreement.model.LoanAgreementInvolvementType
import cn.sunline.saas.loan.agreement.model.db.LoanAgreement
import cn.sunline.saas.loan.agreement.model.dto.DTORepaymentArrangementView
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.loan.product.model.LoanProductType
import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstruction
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstructionType
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.product.service.ProductService
import cn.sunline.saas.repayment.arrangement.service.RepaymentAccountService
import cn.sunline.saas.repayment.arrangement.service.RepaymentArrangementService
import cn.sunline.saas.repayment.instruction.model.dto.DTORepaymentInstruction
import cn.sunline.saas.repayment.instruction.model.dto.DTORepaymentInstructionAdd
import cn.sunline.saas.repayment.instruction.service.RepaymentInstructionService
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeRates
import cn.sunline.saas.rpc.invoke.impl.ProductInvokeImpl
import cn.sunline.saas.rpc.invoke.impl.RatePlanInvokeImpl
import cn.sunline.saas.schedule.Schedule
import cn.sunline.saas.schedule.ScheduleService
import cn.sunline.saas.underwriting.arrangement.service.UnderwritingArrangementService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import javax.transaction.Transactional
import kotlin.random.Random

/**
 * @title: ConsumerLoanService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 14:12
 */
@Service
class ConsumerLoanService(
    private val consumerLoanInvoke: ConsumerLoanInvoke,
    private val consumerLoanPublish: ConsumerLoanPublish,
    private val tenantDateTime: TenantDateTime
) {

    @Autowired
    private lateinit var loanAgreementService: LoanAgreementService

    @Autowired
    private lateinit var underwritingArrangementService: UnderwritingArrangementService

    @Autowired
    private lateinit var disbursementArrangementService: DisbursementArrangementService

    @Autowired
    private lateinit var disbursementInstructionService: DisbursementInstructionService

    @Autowired
    private lateinit var invoiceService: InvoiceService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var repaymentInstructionService: RepaymentInstructionService

    @Autowired
    private lateinit var repaymentArrangementService: RepaymentArrangementService

    @Autowired
    private lateinit var invoiceArrangementService: InvoiceArrangementService

    @Autowired
    private lateinit var productInvokeImpl: ProductInvokeImpl

    @Autowired
    private lateinit var ratePlanInvokeImpl: RatePlanInvokeImpl

    @Autowired
    private lateinit var repaymentAccountService: RepaymentAccountService

    @Autowired
    private lateinit var loanAccountService: LoanAccountService

    @Autowired
    private lateinit var feeArrangementService: FeeArrangementService

    @Autowired
    private lateinit var feeItemService: FeeItemService


    fun createLoanAgreement(applicationId: Long) {
        val customerOffer = consumerLoanInvoke.retrieveCustomerOffer(applicationId)
        val loanProduct = consumerLoanInvoke.retrieveLoanProduct(customerOffer.productId)
        val loanAgreementAggregate = loanAgreementService.registered(
            ConsumerLoanAssembly.convertToDTOLoanAgreementAdd(
                customerOffer, loanProduct, InterestRateHelper.getRate(
                    customerOffer.term, consumerLoanInvoke.retrieveBaseInterestRate(loanProduct.interestFeature.id)
                )?.toPlainString()
            )
        )

        customerOffer.guarantors?.run {
            underwritingArrangementService.registered(
                ConsumerLoanAssembly.convertToDTOUnderwritingArrangementAdd(
                    loanAgreementAggregate, this
                )
            )
        }

        val feeArrangement = loanAgreementAggregate.feeArrangement?.run {
            objectMapper.convertValue<MutableList<DTOFeeArrangementView>>(
                this
            )
        }
        val feeDeductItem = feeArrangementService.getDisbursementFeeDeductItem(
            feeArrangement,
            customerOffer.amount.toBigDecimal()
        )

        val dtoFeeItemAdd = ArrayList<DTOFeeItemAdd>()
        feeArrangement?.filter { it.feeType == LoanFeeType.DISBURSEMENT }?.forEach {
            val feeAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    FeeUtil.calFeeAmount(customerOffer.amount.toBigDecimal(), it.feeRate!!, it.feeMethodType)
                }
                FeeMethodType.FIX_AMOUNT -> {
                    it.feeAmount
                }
            }
            dtoFeeItemAdd.add(
                DTOFeeItemAdd(
                    agreementId = loanAgreementAggregate.loanAgreement.id,
                    feeArrangementId = it.id.toLong(),
                    feeAmount = feeAmount ?: BigDecimal.ZERO,
                    repaymentAmount = BigDecimal.ZERO,
                    feeFromDate = tenantDateTime.toTenantDateTime(loanAgreementAggregate.loanAgreement.fromDateTime).toDate(),
                    feeToDate = tenantDateTime.toTenantDateTime(loanAgreementAggregate.loanAgreement.toDateTime).toDate(),
                    feeUser = null,
                    currencyType = loanAgreementAggregate.loanAgreement.currency,
                )
            )
        }
        feeItemService.registered(loanAgreementAggregate.loanAgreement.id, dtoFeeItemAdd)

        // Calculate Repayment Schedule and create invoices
        val interestRate = loanAgreementAggregate.interestArrangement.getExecutionRate()
        val schedules = ScheduleService(
            customerOffer.amount.toBigDecimal(),
            interestRate,
            customerOffer.term,
            loanProduct.repaymentFeature.payment.frequency,
            loanProduct.repaymentFeature.payment.repaymentDayType,
            loanProduct.interestFeature.interest.baseYearDays,
            tenantDateTime.toTenantDateTime(loanAgreementAggregate.loanAgreement.fromDateTime),
            tenantDateTime.toTenantDateTime(loanAgreementAggregate.loanAgreement.toDateTime),
            null,
            feeDeductItem.scheduleFee
        ).getSchedules(loanProduct.repaymentFeature.payment.paymentMethod)



        invoiceService.initiateLoanInvoice(
            ConsumerLoanAssembly.convertToDTOLoanInvoice(
                schedules, loanAgreementAggregate
            )
        )

        val loanAgreement = loanAgreementService.archiveAgreement(loanAgreementAggregate.loanAgreement)
        signAndLending(loanAgreement)

    }

    fun signLoanAgreementByOffline(agreementId: Long) {
        val loanAgreement =
            loanAgreementService.getOne(agreementId) ?: throw LoanAgreementNotFoundException("loan agreement not found")
        signAndLending(loanAgreement)
    }

    private fun signAndLending(loanAgreement: LoanAgreement) {
        val signLoanAgreement = loanAgreementService.signAgreement(loanAgreement)
        lending(signLoanAgreement.id)
    }

    private fun lending(agreementId: Long) {
        val loanAgreement =
            loanAgreementService.getOne(agreementId) ?: throw LoanAgreementNotFoundException("loan agreement not found")
        if (loanAgreement.status != AgreementStatus.SIGNED) {
            throw LoanAgreementStatusCheckException("loan agreement hasn't signed")
        }
        val disbursementArrangement = disbursementArrangementService.getOne(loanAgreement.id)
            ?: throw DisbursementArrangementNotFoundException("disbursement arrangement not found")

        val dtoDisbursementInstruction = DTODisbursementInstructionAdd(
            moneyTransferInstructionAmount = disbursementArrangementService.calculateLendingAmount(
                loanAgreement.amount, disbursementArrangement
            ),
            moneyTransferInstructionCurrency = loanAgreement.currency,
            moneyTransferInstructionPurpose = loanAgreement.purpose,
            payeeAccount = disbursementArrangement.disbursementAccount,
            payerAccount = null,
            agreementId = agreementId,
            businessUnit = loanAgreement.involvements.first { LoanAgreementInvolvementType.LOAN_LENDER == it.involvementType }.partyId
        )

        val disbursementInstruction = disbursementInstructionService.registered(dtoDisbursementInstruction)

//        val feeArrangement = feeArrangementService.listByAgreementId(loanAgreement.id).run {
//            objectMapper.convertValue<MutableList<DTOFeeArrangementView>>(
//                this
//            )
//        }
//        val feeDeductItem = feeArrangementService.getDisbursementFeeDeductItem(
//            feeArrangement,
//            loanAgreement.amount
//        )

        consumerLoanPublish.initiatePositionKeeping(
            DTOBankingTransaction(
                name = disbursementArrangement.disbursementAccountBank,
                agreementId = disbursementInstruction.agreementId,
                instructionId = disbursementInstruction.id,
                transactionDescription = null,
                currency = loanAgreement.currency,
                amount = loanAgreement.amount,
                businessUnit = disbursementInstruction.businessUnit,
                appliedFee = null,
                appliedRate = null,
                customerId = loanAgreement.involvements.first { it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId,
            )
        )
        // TODO reduce fee to financialAccount And how record the fee Item
        consumerLoanPublish.financialAccounting(disbursementInstruction)
    }

    fun callBackFinancialAccounting(instructionId: Long) {
        val disbursementInstruction = disbursementInstructionService.retrieve(instructionId)
        consumerLoanPublish.disbursement(disbursementInstruction)
    }

    fun callBackDisbursement(instructionId: Long) {
        val disbursementInstruction = disbursementInstructionService.getOne(instructionId)!!
        val loanAgreement = loanAgreementService.getOne(disbursementInstruction.agreementId)
            ?: throw LoanAgreementNotFoundException("loan agreement not found")

        feeItemService.listByAgreementId(loanAgreement.id).forEach {
            it.repaymentAmount = it.feeAmount
            it.repaymentStatus = RepaymentStatus.CLEAR
            it.feeRepaymentDate =tenantDateTime.now().toDate()
            feeItemService.save(it)
        }

        loanAgreement.status = AgreementStatus.PAID
        loanAgreementService.save(loanAgreement)

        disbursementInstruction.moneyTransferInstructionStatus = InstructionLifecycleStatus.FULFILLED
        disbursementInstruction.executeDateTime = tenantDateTime.now().toDate()
        disbursementInstruction.endDateTime = tenantDateTime.now().toDate()
        disbursementInstructionService.save(disbursementInstruction)

    }

    fun getLoanAgreementByApplicationId(applicationId: Long): DTOLoanAgreementView? {
        val loanAgreement = loanAgreementService.findByApplicationId(applicationId)
        return loanAgreement?.run { objectMapper.convertValue(loanAgreement) }
    }

    fun getLoanAgreementInfoByApplicationId(applicationId: Long): DTOLoanAgreementViewInfo? {
        val loanAgreement = loanAgreementService.findByApplicationId(applicationId) ?: return null

        val loanProduct = consumerLoanInvoke.retrieveLoanProduct(loanAgreement.productId)

        val repaymentArrangement = repaymentArrangementService.getOne(loanAgreement.id)
            ?: throw LoanAgreementNotFoundException("repayment arrangement not found")

        val disbursementArrangement = disbursementArrangementService.getOne(loanAgreement.id)
            ?: throw LoanAgreementNotFoundException("disbursement arrangement not found")

        return DTOLoanAgreementViewInfo(
            id = loanAgreement.id.toString(),
            applicationId = loanAgreement.applicationId.toString(),
            productId = loanProduct.id,
            productName = loanProduct.name,
            amount = loanAgreement.amount.toPlainString(),
            term = loanAgreement.term,
            disbursementAccountBank = disbursementArrangement.disbursementAccountBank,
            disbursementAccount = disbursementArrangement.disbursementAccount,
            purpose = loanAgreement.purpose,
            paymentMethod = repaymentArrangement.paymentMethod,
            currency = loanAgreement.currency,
            fromDateTime = tenantDateTime.toTenantDateTime(loanAgreement.fromDateTime).toString(),
            toDateTime = tenantDateTime.toTenantDateTime(loanAgreement.toDateTime).toString(),
            signedDate = tenantDateTime.toTenantDateTime(loanAgreement.signedDate).toString(),
            loanProductType = loanProduct.loanProductType,
            agreementStatus = loanAgreement.status
        )
//        return loanAgreement?.run { objectMapper.convertValue(loanAgreement) }
    }

    fun signedLoanAgreement(applicationId: Long) {
        updateLoanAgreementStatus(applicationId, AgreementStatus.SIGNED)
    }

    fun paidLoanAgreement(applicationId: Long) {
        updateLoanAgreementStatus(applicationId, AgreementStatus.PAID)
    }

    private fun updateLoanAgreementStatus(applicationId: Long, status: AgreementStatus) {
        val loanAgreement = loanAgreementService.findByApplicationId(applicationId)
            ?: throw LoanAgreementNotFoundException("Invalid loan agreement")
        loanAgreement.status = status
        loanAgreementService.save(loanAgreement)
    }

    fun repayLoanInvoiceById(repayAmount: BigDecimal, invoiceId: Long, agreementId: Long) {
        val invoice = invoiceService.getOne(invoiceId) ?: throw InvoiceNotFoundException("invoice not found")
        repayLoanInvoice(repayAmount, invoice, agreementId)
    }

    fun repayLoanInvoice(repayAmount: BigDecimal, invoice: Invoice, agreementId: Long) {
        val loanAgreement =
            loanAgreementService.getOne(agreementId) ?: throw LoanAgreementNotFoundException("loan agreement not found")

        val repaymentAccount = repaymentArrangementService.listRepaymentAccounts(agreementId)[0]
        val repaymentInstruction = repaymentInstructionService.registered(
            DTORepaymentInstructionAdd(
                repayAmount,
                loanAgreement.currency,
                null,
                null,
                repaymentAccount.repaymentAccount,
                agreementId,
                loanAgreement.involvements.first { it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId,
                invoice.id,
                tenantDateTime.now().toDate(),
                null
            )
        )
        val invoiceArrangement = invoiceArrangementService.getOne(agreementId)
            ?: throw InvoiceArrangementNotFoundException("invoice arrangement not found")

        val actualRepayAmount =
            invoiceService.repayInvoice(repayAmount, invoice, invoiceArrangement.graceDays ?: 0, tenantDateTime.now())
        actualRepayAmount[InvoiceAmountType.PRINCIPAL]?.run {
            if (this > BigDecimal.ZERO) {
                consumerLoanPublish.reducePositionKeeping(
                    DTOBankingTransaction(
                        name = repaymentAccount.repaymentAccountBank,
                        agreementId = agreementId,
                        instructionId = repaymentInstruction.id,
                        transactionDescription = null,
                        currency = loanAgreement.currency,
                        amount = this,
                        businessUnit = repaymentInstruction.businessUnit,
                        appliedFee = null,
                        appliedRate = null,
                        customerId = loanAgreement.involvements.first { it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId,
                    )
                )
            }
        }
        consumerLoanPublish.financialAccountingRepayment(
            DTORepaymentInstruction(
                id = repaymentInstruction.id,
                instructionAmount = repaymentInstruction.moneyTransferInstructionAmount.toPlainString(),
                instructionCurrency = repaymentInstruction.moneyTransferInstructionCurrency,
                instructionPurpose = repaymentInstruction.moneyTransferInstructionPurpose,
                payeeAccount = repaymentInstruction.payeeAccount,
                payerAccount = repaymentInstruction.payerAccount,
                agreementId = repaymentInstruction.agreementId,
                businessUnit = repaymentInstruction.businessUnit,
                referenceId = repaymentInstruction.referenceId!!
            )
        )
    }

    fun calculateSchedule(productId: Long, amount: BigDecimal, term: LoanTermType): DTORepaymentScheduleTrialView {

        val loanProduct = productInvokeImpl.getProductInfoByProductId(productId)
        val feeArrangement = loanProduct.feeFeatures?.run {
            objectMapper.convertValue<MutableList<DTOFeeArrangementView>>(
                this
            )
        }
        val feeDeductItem = feeArrangementService.getDisbursementFeeDeductItem(feeArrangement, amount)
        val ratePlanId = loanProduct.interestFeature.ratePlanId
        val interestRate = getInterestRate(loanProduct.interestFeature.interestType, term, ratePlanId.toLong())
        val schedule = ScheduleService(
            amount,
            interestRate,
            term,
            loanProduct.repaymentFeature.payment.frequency,
            loanProduct.repaymentFeature.payment.repaymentDayType,
            loanProduct.interestFeature.interest.baseYearDays,
            tenantDateTime.now(),
            null,
            null,
            feeDeductItem.scheduleFee
        ).getSchedules(loanProduct.repaymentFeature.payment.paymentMethod)
        return convertToScheduleTrialMapper(schedule, feeDeductItem.immediateFee)
    }

    private fun convertToInterestRate(list: List<DTOInvokeRates>, ratePlanId: Long): MutableList<InterestRate> {
        val rates = mutableListOf<InterestRate>()
        for (rate in list) {
            rates.add(
                InterestRate(
                    rate.id.toLong(), rate.period, rate.rate.toBigDecimal(), ratePlanId
                )
            )
        }
        return rates
    }

    private fun getInterestRate(interestType: InterestType, term: LoanTermType, ratePlanId: Long): BigDecimal {
        val rateResult = ratePlanInvokeImpl.getRatePlanByRatePlanId(ratePlanId.toLong())
        val ratesModel = convertToInterestRate(rateResult.rates, ratePlanId.toLong())
        val rate = InterestRateHelper.getRate(term, ratesModel)!!
        val executionRate = when (interestType) {
            InterestType.FIXED -> rate
            InterestType.FLOATING_RATE_NOTE -> {
                val baseRateResult = ratePlanInvokeImpl.getRatePlanByType(RatePlanType.STANDARD)
                val baseRateModel = convertToInterestRate(baseRateResult.rates, ratePlanId.toLong())
                val baseRate = InterestRateHelper.getRate(term, baseRateModel)
                if (baseRate == null) {
                    throw BaseRateNullException("base rate must be not null when interest type is floating rate")
                } else {
                    CalculateInterestRate(baseRate).calRateWithNoPercent(rate)
                }
            }
        }
        return executionRate
    }

    private fun convertToScheduleTrialMapper(
        dtoSchedule: MutableList<Schedule>,
        immediateFee: BigDecimal
    ): DTORepaymentScheduleTrialView {
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailTrialView> = ArrayList()

        val interestRate = dtoSchedule.first().interestRate
        var installment = dtoSchedule.first().instalment
        for (schedule in dtoSchedule) {
            if (installment != schedule.instalment) {
                installment = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            }
            dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailTrialView(
                period = schedule.period,
                installment = schedule.instalment,
                principal = schedule.principal,
                interest = schedule.interest,
                repaymentDate = schedule.dueDate.toString(),
                fee = schedule.fee
            )
        }
        return DTORepaymentScheduleTrialView(
            installment = installment,
            fee = immediateFee,
            interestRate = interestRate,
            schedule = dtoRepaymentScheduleDetailTrialView
        )
    }

    fun addRepaymentAccount(dtoRepaymentAccountAdd: DTORepaymentAccountAdd): DTORepaymentAccountView {
        val repaymentAccount =
            repaymentAccountService.retrieveByRepaymentAccount(dtoRepaymentAccountAdd.repaymentAccount)
        if (!repaymentAccount.isEmpty) {
            throw RepaymentAgreementBusinessException(
                "The repaymentAccount already exists",
                ManagementExceptionCode.DATA_ALREADY_EXIST
            )
        }

        loanAgreementService.addRepaymentAccount(
            dtoRepaymentAccountAdd.agreementId, ConsumerLoanAssembly.convertToDTORepaymentAccountAdd(
                dtoRepaymentAccountAdd.repaymentAccount, dtoRepaymentAccountAdd.repaymentAccountBank
            )
        )

        val repaymentAccountLines =
            repaymentArrangementService.listRepaymentAccounts(dtoRepaymentAccountAdd.agreementId)
        val lines = ArrayList<DTORepaymentAccountLineView>()
        repaymentAccountLines.forEach {
            lines.add(
                objectMapper.convertValue<DTORepaymentAccountLineView>(it)
            )
        }

        return DTORepaymentAccountView(
            agreementId = dtoRepaymentAccountAdd.agreementId.toString(),
            repaymentAccountLines = lines
        )
    }

    fun getRepaymentAccounts(agreementId: Long): DTORepaymentAccountView {
        val repaymentAccountLines = repaymentArrangementService.listRepaymentAccounts(agreementId)
        val lines = ArrayList<DTORepaymentAccountLineView>()
        repaymentAccountLines.forEach {
            lines.add(
                objectMapper.convertValue<DTORepaymentAccountLineView>(it)
            )
        }
        return DTORepaymentAccountView(
            agreementId = agreementId.toString(),
            repaymentAccountLines = lines
        )
    }

    fun retrieveLoanAgreementDetail(agreementId: Long): DTOLoanAgreementDetailView {
        val loanAgreement = loanAgreementService.getOne(agreementId)
            ?: throw LoanAgreementNotFoundException("loan agreement not found")

        val loanProduct = consumerLoanInvoke.retrieveLoanProduct(loanAgreement.productId)

        val repaymentArrangement = repaymentArrangementService.getOne(agreementId)
            ?: throw LoanAgreementNotFoundException("repayment arrangement not found")

        val disbursementArrangement = disbursementArrangementService.getOne(agreementId)
            ?: throw LoanAgreementNotFoundException("disbursement arrangement not found")

        return DTOLoanAgreementDetailView(
            agreementId = agreementId.toString(),
            productName = loanProduct.name,
            amount = loanAgreement.amount.toPlainString(),
            term = loanAgreement.term,
            disbursementAccount = disbursementArrangement.disbursementAccountBank + "(${
                disbursementArrangement.disbursementAccount.substring(
                    disbursementArrangement.disbursementAccount.length - 4,
                    disbursementArrangement.disbursementAccount.length
                )
            })",
            purpose = loanAgreement.purpose,
            paymentMethod = repaymentArrangement.paymentMethod,
            disbursementBank = disbursementArrangement.disbursementAccountBank,
            agreementDocumentId = loanAgreement.agreementDocument,
        )
    }

    fun retrieveBankList(): MutableList<DTOBankListView> {
        val list = ArrayList<DTOBankListView>()
        for (index in 1..10) {
            list += DTOBankListView(
                id = index.toString(),
                name = "bank$index",
                code = index.toString()
            )
        }
        return list
    }

    fun getVerifyCode(mobilePhone: String): DTOVerifyCode {
        var code = ""
        for (i in 1..6) {
            val randoms = Random.nextInt(0, 9)
            code += randoms
        }
        return DTOVerifyCode(
            mobilePhone = mobilePhone, code = code
        )
    }

    fun repayment(dtoInvoiceRepay: DTOInvoiceRepay): DTOInvoiceInfoView {

        val preRepaymentInstruction =
            repaymentInstructionService.getPageByInvoiceId(dtoInvoiceRepay.invoiceId.toLong(), Pageable.unpaged())
        if (!preRepaymentInstruction.isEmpty) {
            throw LoanInvoiceBusinessException(
                "repayment instruction already exists",
                ManagementExceptionCode.REPAYMENT_INSTRUCTION_ERROR
            )
        }

        val invoice = invoiceService.getOne(dtoInvoiceRepay.invoiceId.toLong())
            ?: throw  LoanInvoiceBusinessException("Loan Invoice Not Found")

        val loanAgreement = loanAgreementService.getOne(invoice.agreementId)
            ?: throw LoanAgreementNotFoundException("loan agreement not found")

        val repaymentAccount = repaymentArrangementService.listRepaymentAccounts(invoice.agreementId)
            .first { a -> a.repaymentAccount == dtoInvoiceRepay.repaymentAccount && a.id == dtoInvoiceRepay.repaymentAccountId.toLong() }


        val repaymentInstruction = repaymentInstructionService.registered(
            DTORepaymentInstructionAdd(
                moneyTransferInstructionAmount = dtoInvoiceRepay.amount.toBigDecimal(),
                moneyTransferInstructionCurrency = loanAgreement.currency,
                moneyTransferInstructionPurpose = null,
                payeeAccount = null,
                payerAccount = repaymentAccount.repaymentAccount,
                agreementId = invoice.agreementId,
                businessUnit = loanAgreement.involvements.first { it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId,
                referenceId = invoice.id,
                startDate = tenantDateTime.now().toDate(),
                operator = ContextUtil.getUserId()
            )
        )

        consumerLoanPublish.financialAccountingRepayment(
            DTORepaymentInstruction(
                id = repaymentInstruction.id,
                instructionAmount = repaymentInstruction.moneyTransferInstructionAmount.toPlainString(),
                instructionCurrency = repaymentInstruction.moneyTransferInstructionCurrency,
                instructionPurpose = repaymentInstruction.moneyTransferInstructionPurpose,
                payeeAccount = repaymentInstruction.payeeAccount,
                payerAccount = repaymentInstruction.payerAccount,
                agreementId = repaymentInstruction.agreementId,
                businessUnit = repaymentInstruction.businessUnit,
                referenceId = repaymentInstruction.referenceId!!
            )
        )

        val lines = ArrayList<DTOInvoiceLinesView>()
        invoice.invoiceLines.map {
            lines += DTOInvoiceLinesView(
                invoiceAmountType = it.invoiceAmountType,
                invoiceAmount = it.invoiceAmount.toPlainString()
            )
        }
        return DTOInvoiceInfoView(
            invoicee = invoice.invoicee.toString(),
            invoiceId = invoice.id.toString(),
            invoiceDueDate = invoice.invoiceRepaymentDate.toString(),
            invoicePeriodFromDate = invoice.invoicePeriodFromDate.toString(),
            invoicePeriodToDate = invoice.invoicePeriodToDate.toString(),
            invoiceTotalAmount = invoice.invoiceAmount.toPlainString(),
            invoiceCurrency = loanAgreement.currency,
            invoiceStatus = invoice.invoiceStatus,
            invoiceLines = lines
        )
    }

    fun calculatePrepayment(agreementId: Long): DTOPreRepaymentTrailView {
        val agreement =
            loanAgreementService.getOne(agreementId) ?: throw LoanInvoiceBusinessException("Loan Agreement Not Found")

        val repaymentArrangement = repaymentArrangementService.getOne(agreementId)
            ?: throw LoanInvoiceBusinessException("repayment Agreement Not Found")

        val account = loanAccountService.getAccount(agreementId)
            ?: throw AccountNotFoundException("account not found")

        var amount = BigDecimal.ZERO
        account.accountBalance.forEach {
            if (AccountBalanceType.LOAN_OUTSTANDING_AMOUNT == it.accountBalanceType) {
                amount = amount.add(it.accountBalance)
            }
        }

        val invoice = invoiceService.listInvoiceByAgreementId(agreementId, Pageable.unpaged())
            .filter { it.invoiceStatus == InvoiceStatus.INITIATE }.minByOrNull { it.invoicePeriodFromDate }!!

        val feeArrangements = feeArrangementService.listByAgreementId(agreementId)
        val feeArrangement = feeArrangements.run {
            objectMapper.convertValue<MutableList<DTOFeeArrangementView>>(
                this
            )
        }
        val feeDeductItem = feeArrangementService.getPrepaymentFeeDeductItem(feeArrangement, amount)

        val loanProduct = productInvokeImpl.getProductInfoByProductId(agreement.productId)
        val ratePlanId = loanProduct.interestFeature.ratePlanId
        val interestRate =
            getInterestRate(loanProduct.interestFeature.interestType, agreement.term, ratePlanId.toLong())
        val schedule = ScheduleService(
            amount,
            interestRate,
            agreement.term,
            repaymentArrangement.frequency,
            loanProduct.repaymentFeature.payment.repaymentDayType,
            loanProduct.interestFeature.interest.baseYearDays,
            tenantDateTime.toTenantDateTime(invoice.invoicePeriodFromDate),
            tenantDateTime.toTenantDateTime(agreement.toDateTime),
            tenantDateTime.now(),
            invoice.invoiceLines.filter { it.invoiceAmountType == InvoiceAmountType.FEE }.sumOf { it.invoiceAmount }
                .add(feeDeductItem.immediateFee)
        ).getPrepaymentSchedules(repaymentArrangement.paymentMethod)

        val prepaymentLines = convertToInvoiceLines(schedule)
        return DTOPreRepaymentTrailView(
            agreementId = agreementId.toString(),
            totalAmount = prepaymentLines.sumOf { it.invoiceAmount.toBigDecimal() }.toPlainString(),
            prepaymentLines = prepaymentLines
        )
    }

    private fun convertToInvoiceLines(schedule: MutableList<Schedule>): ArrayList<DTOInvoiceLinesView> {
        var totalPrincipal = BigDecimal.ZERO
        var totalInterest = BigDecimal.ZERO
        var totalFee = BigDecimal.ZERO
        val totalFine = BigDecimal.ZERO

        val prepaymentLines = ArrayList<DTOInvoiceLinesView>()
        schedule.forEach {
            totalPrincipal = totalPrincipal.add(it.principal)
            totalInterest = totalInterest.add(it.interest)
            totalFee = totalFee.add(it.fee)
        }

        // TODO Calculate Prepayment Penalty Fee
        InvoiceAmountType.values().forEach {
            if (InvoiceAmountType.PRINCIPAL == it) {
                prepaymentLines.add(
                    DTOInvoiceLinesView(
                        invoiceAmountType = it, invoiceAmount = totalPrincipal.toPlainString()
                    )
                )
            } else if (InvoiceAmountType.INTEREST == it) {
                prepaymentLines.add(
                    DTOInvoiceLinesView(
                        invoiceAmountType = it, invoiceAmount = totalInterest.toPlainString()
                    )
                )
            } else if (InvoiceAmountType.PENALTY_INTEREST == it) {
                prepaymentLines.add(
                    DTOInvoiceLinesView(
                        invoiceAmountType = it,
                        invoiceAmount = totalFine.toPlainString()
                    )
                )
            } else if (InvoiceAmountType.FEE == it) {
                prepaymentLines.add(
                    DTOInvoiceLinesView(
                        invoiceAmountType = it, invoiceAmount = totalFee.toPlainString()
                    )
                )
            } else {
//                throw LoanInvoiceBusinessException("InvoiceAmount Type Not Found")
            }
        }
        return prepaymentLines
    }

    fun prepayment(dtoPrepayment: DTOPrepayment) {

        val preRepaymentInstruction = repaymentInstructionService.getPage(
            dtoPrepayment.agreementId, null, MoneyTransferInstructionType.REPAYMENT, null,
            Pageable.unpaged()
        ).filter {
            it.moneyTransferInstructionStatus != InstructionLifecycleStatus.FULFILLED &&
                    it.moneyTransferInstructionStatus != InstructionLifecycleStatus.FAILED
        }
        if (!preRepaymentInstruction.isEmpty) {
            throw LoanInvoiceBusinessException(
                "repayment instruction already exists",
                ManagementExceptionCode.REPAYMENT_INSTRUCTION_ERROR
            )
        }

        val loanAgreement = loanAgreementService.getOne(dtoPrepayment.agreementId)
            ?: throw LoanAgreementNotFoundException("loan agreement not found")

        val repayAmount = dtoPrepayment.principal.toBigDecimal()
            .add(dtoPrepayment.interest.toBigDecimal())
            .add(dtoPrepayment.fee.toBigDecimal())

        val repaymentAccount = repaymentArrangementService.listRepaymentAccounts(dtoPrepayment.agreementId)
            .firstOrNull { it.id == dtoPrepayment.repaymentAccountId && it.repaymentAccount == dtoPrepayment.repaymentAccount }
            ?: throw LoanInvoiceBusinessException("repayment account not found")

        val dtoLoanInvoice = mutableListOf<DTOLoanInvoice>()
        val repaymentDate = tenantDateTime.now().toString()
        // TODO Confirm period value
        dtoLoanInvoice.add(
            DTOLoanInvoice(
                period = 0,
                invoicePeriodFromDate = repaymentDate,
                invoicePeriodToDate = repaymentDate,
                invoicee = loanAgreement.involvements.first { involvement -> LoanAgreementInvolvementType.LOAN_BORROWER == involvement.involvementType }.partyId,
                principal = dtoPrepayment.principal.toBigDecimal(),
                interest = dtoPrepayment.interest.toBigDecimal(),
                fee = dtoPrepayment.fee.toBigDecimal(),
                agreementId = dtoPrepayment.agreementId,
                invoiceStatus = InvoiceStatus.TEMP
            )
        )
        val invoice = invoiceService.initiateLoanInvoice(dtoLoanInvoice).first()

        val feeArrangements = feeArrangementService.listByAgreementId(dtoPrepayment.agreementId)
        val feeArrangement = feeArrangements.run {
            objectMapper.convertValue<MutableList<DTOFeeArrangementView>>(
                this
            )
        }

        val dtoFeeItemAdd = ArrayList<DTOFeeItemAdd>()
        feeArrangement.filter { it.feeType == LoanFeeType.PREPAYMENT }.forEach {
            val feeAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    FeeUtil.calFeeAmount( dtoPrepayment.principal.toBigDecimal(), it.feeRate!!, it.feeMethodType)
                }
                FeeMethodType.FIX_AMOUNT -> {
                    it.feeAmount
                }
            }
            dtoFeeItemAdd.add(
                DTOFeeItemAdd(
                    agreementId = dtoPrepayment.agreementId,
                    feeArrangementId = it.id.toLong(),
                    feeAmount = feeAmount ?: BigDecimal.ZERO,
                    repaymentAmount = BigDecimal.ZERO,
                    feeFromDate = tenantDateTime.toTenantDateTime(invoice.invoicePeriodFromDate).toDate(),
                    feeToDate = tenantDateTime.toTenantDateTime(loanAgreement.toDateTime).toDate(),
                    feeUser = null,
                    currencyType = loanAgreement.currency,
                )
            )
        }
        feeItemService.registered(dtoPrepayment.agreementId, dtoFeeItemAdd)

        val repaymentInstruction = repaymentInstructionService.registered(
            DTORepaymentInstructionAdd(
                moneyTransferInstructionAmount = repayAmount,
                moneyTransferInstructionCurrency = loanAgreement.currency,
                moneyTransferInstructionPurpose = null,
                payeeAccount = null,
                payerAccount = repaymentAccount.repaymentAccount,
                agreementId = dtoPrepayment.agreementId,
                businessUnit = loanAgreement.involvements.first { it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId,
                referenceId = invoice.id,
                startDate = tenantDateTime.now().toDate(),
                operator = ContextUtil.getUserId()
            )
        )

        // TODO Call financial Account
        consumerLoanPublish.financialAccountingPrepayment(
            DTORepaymentInstruction(
                id = repaymentInstruction.id,
                instructionAmount = repaymentInstruction.moneyTransferInstructionAmount.toPlainString(),
                instructionCurrency = repaymentInstruction.moneyTransferInstructionCurrency,
                instructionPurpose = repaymentInstruction.moneyTransferInstructionPurpose,
                payeeAccount = repaymentInstruction.payeeAccount,
                payerAccount = repaymentInstruction.payerAccount,
                agreementId = repaymentInstruction.agreementId,
                businessUnit = repaymentInstruction.businessUnit,
                referenceId = repaymentInstruction.referenceId!!
            )
        )
    }

    fun callBackFinancialAccountingRepayment(instructionId: Long) {
        val repaymentInstruction = repaymentInstructionService.retrieve(instructionId)
        consumerLoanPublish.repayment(repaymentInstruction)
    }

    fun callBackRepayment(instructionId: Long) {
        val repaymentInstruction = repaymentInstructionService.retrieve(instructionId)
        callBackRepayLoanInvoice(repaymentInstruction)
        // TODO InstructionLifecycleStatus Change
    }

    fun callBackFinancialAccountingPrepayment(instructionId: Long) {
        val repaymentInstruction = repaymentInstructionService.retrieve(instructionId)
        consumerLoanPublish.prepayment(repaymentInstruction)
    }

    fun callBackPrepayment(instructionId: Long) {
        val repaymentInstruction = repaymentInstructionService.retrieve(instructionId)
        callBackRepayLoanInvoice(repaymentInstruction)
        cancelInitLoanInvoice(repaymentInstruction.agreementId, repaymentInstruction.referenceId)
        // TODO InstructionLifecycleStatus Change
    }

    private fun cancelInitLoanInvoice(agreementId: Long, invoiceId: Long) {
        val invoiceList = invoiceService.listInvoiceByAgreementId(agreementId, Pageable.unpaged())
            .toMutableList()
        invoiceList.filter { it.invoiceStatus != InvoiceStatus.FINISHED || it.id != invoiceId }
            .forEach {
                it.invoiceStatus = InvoiceStatus.CANCEL
                invoiceService.save(it)
            }
    }

    private fun callBackRepayLoanInvoice(dtoRepaymentInstruction: DTORepaymentInstruction) {
        val invoice = invoiceService.getOne(dtoRepaymentInstruction.referenceId)
            ?: throw InvoiceArrangementNotFoundException("invoice not found")

        //repayLoanInvoice(dtoRepaymentInstruction.instructionAmount.toBigDecimal(), invoice, invoice.agreementId)

        val repaymentAccount = repaymentArrangementService.listRepaymentAccounts(invoice.agreementId)
            .first { it.repaymentAccount == dtoRepaymentInstruction.payerAccount }

        val invoiceArrangement = invoiceArrangementService.getOne(invoice.agreementId)
            ?: throw InvoiceArrangementNotFoundException("invoice arrangement not found")

        val loanAgreement = loanAgreementService.getOne(invoice.agreementId)
            ?: throw LoanAgreementNotFoundException("loan agreement not found")

        val actualRepayAmount = invoiceService.repayInvoice(
            dtoRepaymentInstruction.instructionAmount.toBigDecimal(),
            invoice,
            invoiceArrangement.graceDays ?: 0,
            tenantDateTime.now()
        )
        actualRepayAmount[InvoiceAmountType.PRINCIPAL]?.run {
            if (this > BigDecimal.ZERO) {
                consumerLoanPublish.reducePositionKeeping(
                    DTOBankingTransaction(
                        name = repaymentAccount.repaymentAccountBank,
                        agreementId = invoice.agreementId,
                        instructionId = dtoRepaymentInstruction.id,
                        transactionDescription = null,
                        currency = dtoRepaymentInstruction.instructionCurrency,
                        amount = this,
                        businessUnit = dtoRepaymentInstruction.businessUnit,
                        appliedFee = null,
                        appliedRate = null,
                        customerId = loanAgreement.involvements.first { it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId,
                    )
                )
            }
        }
    }

    fun fulfillLoanInvoiceRepayment(instructionId: Long) {

        // TODO consistency
        val repaymentInstruction = repaymentInstructionService.getOne(instructionId)
            ?: throw LoanInvoiceBusinessException("repayment instruction not found")

        if (InstructionLifecycleStatus.FULFILLED == repaymentInstruction.moneyTransferInstructionStatus) {
            throw LoanInvoiceBusinessException(
                "repayment instruction  was FULFILLED,non-supported update",
                ManagementExceptionCode.REPAYMENT_INSTRUCTION_STATUS_ERROR
            )
        }

        val invoice = repaymentInstruction.referenceId?.let { invoiceService.getOne(it) }
            ?: throw InvoiceArrangementNotFoundException("invoice not found")

        val dtoRepaymentInstruction = DTORepaymentInstruction(
            id = repaymentInstruction.id,
            instructionAmount = repaymentInstruction.moneyTransferInstructionAmount.toPlainString(),
            instructionCurrency = repaymentInstruction.moneyTransferInstructionCurrency,
            instructionPurpose = repaymentInstruction.moneyTransferInstructionPurpose,
            payeeAccount = repaymentInstruction.payeeAccount,
            payerAccount = repaymentInstruction.payerAccount,
            agreementId = repaymentInstruction.agreementId,
            businessUnit = repaymentInstruction.businessUnit,
            referenceId = repaymentInstruction.referenceId!!
        )
        callBackRepayLoanInvoice(dtoRepaymentInstruction)

        val invoiceRepaymentDate =
            tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(invoice.invoiceRepaymentDate))
        val invoicePeriodFromDate =
            tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(invoice.invoicePeriodFromDate))
        val invoicePeriodToDate =
            tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(invoice.invoicePeriodToDate))
        if (invoiceRepaymentDate == invoicePeriodFromDate && invoiceRepaymentDate == invoicePeriodToDate) {
            cancelInitLoanInvoice(repaymentInstruction.agreementId, repaymentInstruction.referenceId!!)
        }

        repaymentInstruction.moneyTransferInstructionStatus = InstructionLifecycleStatus.FULFILLED
        repaymentInstruction.executeDateTime = tenantDateTime.now().toDate()
        repaymentInstruction.endDateTime = tenantDateTime.now().toDate()
        repaymentInstructionService.save(repaymentInstruction)
    }

    fun failLoanInvoiceRepayment(instructionId: Long) {
        // TODO consistency

        val repaymentInstruction = repaymentInstructionService.getOne(instructionId)
            ?: throw LoanInvoiceBusinessException("repayment instruction not found")

        if (InstructionLifecycleStatus.FAILED == repaymentInstruction.moneyTransferInstructionStatus) {
            throw LoanInvoiceBusinessException(
                "repayment instruction  was FAILED,non-supported update",
                ManagementExceptionCode.REPAYMENT_INSTRUCTION_STATUS_ERROR
            )
        }

        val invoice = repaymentInstruction.referenceId?.let { invoiceService.getOne(it) }
            ?: throw InvoiceArrangementNotFoundException("invoice not found")

        val invoiceRepaymentDate =
            tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(invoice.invoiceRepaymentDate))
        val invoicePeriodFromDate =
            tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(invoice.invoicePeriodFromDate))
        val invoicePeriodToDate =
            tenantDateTime.getYearMonthDay(tenantDateTime.toTenantDateTime(invoice.invoicePeriodToDate))
        if (invoiceRepaymentDate == invoicePeriodFromDate && invoiceRepaymentDate == invoicePeriodToDate) {
            invoice.invoiceStatus = InvoiceStatus.CANCEL
            invoiceService.save(invoice)
        }

        repaymentInstruction.moneyTransferInstructionStatus = InstructionLifecycleStatus.FAILED
        repaymentInstruction.executeDateTime = tenantDateTime.now().toDate()
        repaymentInstruction.endDateTime = tenantDateTime.now().toDate()
        repaymentInstructionService.save(repaymentInstruction)
    }

    fun getLoanAgreementInfoByAgreementId(agreementId: Long): DTOLoanAgreementViewInfo? {
        val loanAgreement = loanAgreementService.getOne(agreementId) ?: return null

        val loanProduct = consumerLoanInvoke.retrieveLoanProduct(loanAgreement.productId)

        val repaymentArrangement = repaymentArrangementService.getOne(agreementId)
            ?: throw LoanAgreementNotFoundException("repayment arrangement not found")

        val disbursementArrangement = disbursementArrangementService.getOne(agreementId)
            ?: throw LoanAgreementNotFoundException("disbursement arrangement not found")

        return DTOLoanAgreementViewInfo(
            id = agreementId.toString(),
            applicationId = loanAgreement.applicationId.toString(),
            productId = loanProduct.id,
            productName = loanProduct.name,
            amount = loanAgreement.amount.toPlainString(),
            term = loanAgreement.term,
            disbursementAccountBank = disbursementArrangement.disbursementAccountBank,
            disbursementAccount = disbursementArrangement.disbursementAccount,
            purpose = loanAgreement.purpose,
            paymentMethod = repaymentArrangement.paymentMethod,
            currency = loanAgreement.currency,
            fromDateTime = tenantDateTime.toTenantDateTime(loanAgreement.fromDateTime).toString(),
            toDateTime = tenantDateTime.toTenantDateTime(loanAgreement.toDateTime).toString(),
            signedDate = tenantDateTime.toTenantDateTime(loanAgreement.signedDate).toString(),
            loanProductType = loanProduct.loanProductType,
            agreementStatus = loanAgreement.status
        )
    }

    fun getRepaymentInstructionRecord(customerId: Long): MutableList<DTORepaymentRecordView> {
        val repaymentInstructionPage = repaymentInstructionService.getPage(
            null,
            customerId,
            MoneyTransferInstructionType.REPAYMENT,
            null,
            Pageable.unpaged()
        )
        val list = ArrayList<DTORepaymentRecordView>()
        repaymentInstructionPage.sortedByDescending { i -> i.startDateTime }.forEach { record ->
            list.add(
                DTORepaymentRecordView(
                    id = record.id.toString(),
                    repaymentAmount = record.moneyTransferInstructionAmount,
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
            )
        }
        return list
    }
    
    fun getFeeItemListByAgreementId(agreementId: Long): List<DTOFeeItemView> {
        return feeItemService.listByAgreementId(agreementId).map {
            val feeArrangement = feeArrangementService.getOne(it.feeArrangementId)
                ?: throw LoanAgreementNotFoundException("fee arrangement not found")
            DTOFeeItemView(
                agreementId = agreementId.toString(),
                loanFeeType = feeArrangement.feeType,
                loanFeeTypeName = feeArrangement.feeType.name,
                currency = it.currency,
                feeAmountOrRatio = it.feeAmount.toPlainString(),
                nonPaymentAmount = it.feeAmount.subtract(it.repaymentAmount).toPlainString()
            )
        }
    }
}