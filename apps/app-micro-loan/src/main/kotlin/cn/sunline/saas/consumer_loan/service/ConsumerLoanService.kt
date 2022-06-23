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
import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.formula.constant.CalculatePrecision
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
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
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
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
        // Calculate Repayment Schedule and create invoices
        val interestRate = loanAgreementAggregate.interestArrangement.getExecutionRate()
        val schedules = ScheduleService(
            BigDecimal(customerOffer.amount),
            interestRate,
            customerOffer.term,
            loanProduct.repaymentFeature.payment.frequency,
            tenantDateTime.toTenantDateTime(loanAgreementAggregate.loanAgreement.fromDateTime),
            tenantDateTime.toTenantDateTime(loanAgreementAggregate.loanAgreement.toDateTime),
            loanProduct.interestFeature.interest.baseYearDays,
            null
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
        consumerLoanPublish.financialAccounting(disbursementInstruction)
    }

    fun callBackFinancialAccounting(instructionId: Long) {
        val disbursementInstruction = disbursementInstructionService.retrieve(instructionId)
        consumerLoanPublish.disbursement(disbursementInstruction)
    }

    fun callBackDisbursement(instructionId: Long) {
        val disbursementInstruction = disbursementInstructionService.retrieve(instructionId)
        val loanAgreement = loanAgreementService.getOne(disbursementInstruction.agreementId)
            ?: throw LoanAgreementNotFoundException("loan agreement not found")

        loanAgreement.status = AgreementStatus.PAID
        loanAgreementService.save(loanAgreement)
    }

    fun getLoanAgreementByApplicationId(applicationId: Long): DTOLoanAgreementView? {
        val loanAgreement = loanAgreementService.findByApplicationId(applicationId)
        return loanAgreement?.run { objectMapper.convertValue(loanAgreement) }
    }

    fun getLoanAgreementInfoByApplicationId(applicationId: Long): DTOLoanAgreementViewInfo? {
        val loanAgreement = loanAgreementService.findByApplicationId(applicationId)
        return loanAgreement?.run { objectMapper.convertValue(loanAgreement) }
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
                loanAgreement.involvements.first { it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId
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
            )
        )
    }

    fun calculate(productId: Long, amount: BigDecimal, term: LoanTermType): DTORepaymentScheduleTrialView {

        val loanProduct = productInvokeImpl.getProductInfoByProductId(productId)
        val ratePlanId = loanProduct.interestFeature.ratePlanId
        val interestRate = getExecutionRate(loanProduct.interestFeature.interestType, term, ratePlanId.toLong())
        val schedule = ScheduleService(
            amount,
            interestRate,
            term,
            loanProduct.repaymentFeature.payment.frequency,
            tenantDateTime.now(),
            null,
            loanProduct.interestFeature.interest.baseYearDays,
            null,
        ).getSchedules(loanProduct.repaymentFeature.payment.paymentMethod)

        return convertMapper(schedule)
    }

    private fun convertRate(list: List<DTOInvokeRates>, ratePlanId: Long): MutableList<InterestRate> {
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

    private fun getExecutionRate(interestType: InterestType, term: LoanTermType, ratePlanId: Long): BigDecimal {

        val rateResult = ratePlanInvokeImpl.getRatePlanByRatePlanId(ratePlanId.toLong())
        val ratesModel = convertRate(rateResult.rates, ratePlanId.toLong())
        val rate = InterestRateHelper.getRate(term, ratesModel)!!
        val executionRate = when (interestType) {
            InterestType.FIXED -> rate
            InterestType.FLOATING_RATE_NOTE -> {
                val baseRateResult = ratePlanInvokeImpl.getRatePlanByType(RatePlanType.STANDARD)
                val baseRateModel = convertRate(baseRateResult.rates, ratePlanId.toLong())
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

    private fun convertMapper(dtoSchedule: MutableList<Schedule>): DTORepaymentScheduleTrialView {
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
                repaymentDate = schedule.dueDate.toString()
            )
        }
        return DTORepaymentScheduleTrialView(
            installment = installment, interestRate = interestRate, schedule = dtoRepaymentScheduleDetailTrialView
        )
    }

    fun addRepaymentAccount(dtoRepaymentAccountAdd: DTORepaymentAccountAdd): DTORepaymentAccountView {

        val repaymentAccount =
            repaymentAccountService.retrieveByRepaymentAccount(dtoRepaymentAccountAdd.repaymentAccount)
        if (!repaymentAccount.isEmpty) {
            throw RepaymentAgreementBusinessException(
                "The repaymentAccount already exists", ManagementExceptionCode.DATA_ALREADY_EXIST
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
            agreementId = dtoRepaymentAccountAdd.agreementId.toString(), repaymentAccountLines = lines
        )
    }

    fun retrieveLoanAgreementDetail(agreementId: Long): DTOLoanAgreementDetailView {

        val loanAgreement =
            loanAgreementService.getOne(agreementId) ?: throw LoanAgreementNotFoundException("loan agreement not found")

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
                id = index.toString(), name = "bank" + index, code = index.toString()
            )
        }
        return list
    }

    fun prepayment(dtoPrepayment: DTOPrepayment) {
        val loanAgreement = loanAgreementService.getOne(dtoPrepayment.agreementId)
            ?: throw LoanAgreementNotFoundException("loan agreement not found")

        val repayAmount = dtoPrepayment.principal.toBigDecimal().add(dtoPrepayment.interest.toBigDecimal())
            .add(dtoPrepayment.fee.toBigDecimal())

        val repaymentAccount = repaymentArrangementService.listRepaymentAccounts(dtoPrepayment.agreementId)
            .firstOrNull { it.id == dtoPrepayment.repaymentAccountId }
            ?: throw LoanInvoiceBusinessException("repayment account not found")

        val dtoLoanInvoice = mutableListOf<DTOLoanInvoice>()
        val repaymentDate = tenantDateTime.now().toString()
        dtoLoanInvoice.add(
            DTOLoanInvoice(
                repaymentDate,
                repaymentDate,
                loanAgreement.involvements.first { involvement -> LoanAgreementInvolvementType.LOAN_BORROWER == involvement.involvementType }.partyId,
                dtoPrepayment.principal.toBigDecimal(),
                dtoPrepayment.interest.toBigDecimal(),
                dtoPrepayment.fee.toBigDecimal(),
                dtoPrepayment.agreementId
            )
        )
        invoiceService.initiateLoanInvoice(dtoLoanInvoice).first()

        val repaymentInstruction = repaymentInstructionService.registered(
            DTORepaymentInstructionAdd(
                repayAmount,
                loanAgreement.currency,
                null,
                null,
                repaymentAccount.repaymentAccount,
                dtoPrepayment.agreementId,
                loanAgreement.involvements.first { it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId
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
        ))
    }

    fun repayment(dtoInvoiceRepay: DTOInvoiceRepay): DTOInvoiceInfoView {
        val invoice = invoiceService.getOne(dtoInvoiceRepay.invoiceId.toLong())
            ?: throw  LoanInvoiceBusinessException("Loan Invoice Not Found")

        val loanAgreement = loanAgreementService.getOne(invoice.agreementId)
            ?: throw LoanAgreementNotFoundException("loan agreement not found")

        val repaymentAccount = repaymentArrangementService.listRepaymentAccounts(invoice.agreementId)
            .first { a -> a.repaymentAccount == dtoInvoiceRepay.repaymentAccount }

        val repaymentInstruction = repaymentInstructionService.registered(
            DTORepaymentInstructionAdd(
                dtoInvoiceRepay.amount.toBigDecimal(),
                loanAgreement.currency,
                null,
                null,
                repaymentAccount.repaymentAccount,
                invoice.agreementId,
                loanAgreement.involvements.first { it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId
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
            )
        )

        val lines = ArrayList<DTOInvoiceLinesView>()
        invoice.invoiceLines.map {
            lines += DTOInvoiceLinesView(
                invoiceAmountType = it.invoiceAmountType, invoiceAmount = it.invoiceAmount.toPlainString()
            )
        }
       return DTOInvoiceInfoView(
            invoicee = invoice.invoicee.toString(),
            invoiceId = invoice.id.toString(),
            invoiceDueDate = invoice.invoiceRepaymentDate.toString(),
            invoicePeriodFromDate = invoice.invoicePeriodFromDate.toString(),
            invoicePeriodToDate = invoice.invoicePeriodToDate.toString(),
            invoiceTotalAmount = invoice.invoiceAmount,
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

        val account = loanAccountService.getAccount(agreementId) ?: throw AccountNotFoundException("account not found")

        var amount = BigDecimal.ZERO
        account.accountBalance.forEach {
            if (AccountBalanceType.LOAN_OUTSTANDING_AMOUNT == it.accountBalanceType) {
                amount = amount.add(it.accountBalance)
            }
        }

      //  val loanProduct = productInvokeImpl.getProductInfoByProductId(agreement.productId)
       // val ratePlanId = loanProduct.interestFeature.ratePlanId
//        val interestRate =
//            getExecutionRate(loanProduct.interestFeature.interestType, agreement.term, ratePlanId.toLong())

        val schedule = ScheduleService(
            amount,
            //interestRate,
            BigDecimal("5.000000"),
            agreement.term,
            repaymentArrangement.frequency,
            tenantDateTime.toTenantDateTime(agreement.fromDateTime),
            tenantDateTime.toTenantDateTime(agreement.toDateTime),
            //loanProduct.interestFeature.interest.baseYearDays,
            BaseYearDays.ACCOUNT_YEAR,
            tenantDateTime.now()
        ).getResetSchedules(repaymentArrangement.paymentMethod)

        var totalPrincipal = BigDecimal.ZERO
        var totalInterest = BigDecimal.ZERO
        val totalFee = BigDecimal.ZERO
        val totalFine = BigDecimal.ZERO

        val prepaymentLines = ArrayList<DTOInvoiceLinesView>()
        schedule.forEach {
            totalPrincipal = totalPrincipal.add(it.principal)
            totalInterest = totalInterest.add(it.interest)
        }

        // TODO Calculate Prepayment Penalty Fee
        InvoiceAmountType.values().forEach {
            if (InvoiceAmountType.PRINCIPAL == it) {
                prepaymentLines.add(
                    DTOInvoiceLinesView(
                        invoiceAmountType = InvoiceAmountType.PRINCIPAL, invoiceAmount = totalPrincipal.toPlainString()
                    )
                )
            } else if (InvoiceAmountType.INTEREST == it) {
                prepaymentLines.add(
                    DTOInvoiceLinesView(
                        invoiceAmountType = InvoiceAmountType.INTEREST, invoiceAmount = totalInterest.toPlainString()
                    )
                )
            } else if (InvoiceAmountType.PENALTY_INTEREST == it) {
                prepaymentLines.add(
                    DTOInvoiceLinesView(
                        invoiceAmountType = InvoiceAmountType.PENALTY_INTEREST, invoiceAmount = totalFine.toPlainString()
                    )
                )
            } else if (InvoiceAmountType.FEE == it) {
                prepaymentLines.add(
                    DTOInvoiceLinesView(
                        invoiceAmountType = InvoiceAmountType.FEE, invoiceAmount = totalFee.toPlainString()
                    )
                )
            } else {
                throw LoanInvoiceBusinessException("InvoiceAmount Type Not Found")
            }
        }

        return DTOPreRepaymentTrailView(
            agreementId = agreementId.toString(),
            totalAmount = totalPrincipal.add(totalInterest).add(totalFine).add(totalFee).toPlainString(),
            prepaymentLines = prepaymentLines
        )
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

    fun getRepaymentAccounts(agreementId: Long): DTORepaymentAccountView {
        val repaymentAccountLines = repaymentArrangementService.listRepaymentAccounts(agreementId)
        val lines = ArrayList<DTORepaymentAccountLineView>()
        repaymentAccountLines.forEach {
            lines.add(
                objectMapper.convertValue<DTORepaymentAccountLineView>(it)
            )
        }
        return DTORepaymentAccountView(
            agreementId = agreementId.toString(), repaymentAccountLines = lines
        )
    }

    fun callBackFinancialAccountingRepayment(instructionId: Long) {
        val repaymentInstruction = repaymentInstructionService.retrieve(instructionId)
        consumerLoanPublish.repayment(repaymentInstruction)
    }

    fun callBackRepayment(instructionId: Long) {
        val repaymentInstruction = repaymentInstructionService.retrieve(instructionId)
        val invoice = invoiceService.listInvoiceByAgreementId(repaymentInstruction.agreementId, Pageable.unpaged())
            .filter{ it.invoiceStatus== InvoiceStatus.ACCOUNTED }.first()

        repayLoanInvoice(repaymentInstruction.instructionAmount.toBigDecimal(), invoice, invoice.agreementId)

        val repaymentAccount = repaymentArrangementService.listRepaymentAccounts(invoice.agreementId)
            .first { it.repaymentAccount == repaymentInstruction.payerAccount }

        val invoiceArrangement = invoiceArrangementService.getOne(invoice.agreementId)
            ?: throw InvoiceArrangementNotFoundException("invoice arrangement not found")

        val loanAgreement =
            loanAgreementService.getOne(invoice.agreementId) ?: throw LoanAgreementNotFoundException("loan agreement not found")

        val actualRepayAmount =
            invoiceService.repayInvoice(repaymentInstruction.instructionAmount.toBigDecimal(), invoice, invoiceArrangement.graceDays ?: 0, tenantDateTime.now())
        actualRepayAmount[InvoiceAmountType.PRINCIPAL]?.run {
            if (this > BigDecimal.ZERO) {
                consumerLoanPublish.reducePositionKeeping(
                    DTOBankingTransaction(
                        name = repaymentAccount.repaymentAccountBank,
                        agreementId = invoice.agreementId,
                        instructionId = repaymentInstruction.id,
                        transactionDescription = null,
                        currency = repaymentInstruction.instructionCurrency,
                        amount = this,
                        businessUnit = repaymentInstruction.businessUnit,
                        appliedFee = null,
                        appliedRate = null,
                        customerId = loanAgreement.involvements.first { it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId,
                    )
                )
            }
        }
    }

    fun callBackFinancialAccountingPrepayment(instructionId: Long) {
        val repaymentInstruction = repaymentInstructionService.retrieve(instructionId)
        consumerLoanPublish.prepayment(repaymentInstruction)
    }

    fun callBackPrepayment(instructionId: Long) {
        val repaymentInstruction = repaymentInstructionService.retrieve(instructionId)
//        val invoiceList = invoiceService.listInvoiceByAgreementId(repaymentInstruction.agreementId, Pageable.unpaged()).toMutableList()

        val invoice = invoiceService.listInvoiceByAgreementId(repaymentInstruction.agreementId, Pageable.unpaged())
            .filter{ it.invoiceStatus== InvoiceStatus.ACCOUNTED }.first()

        repayLoanInvoice(repaymentInstruction.instructionAmount.toBigDecimal(), invoice, invoice.agreementId)

        val repaymentAccount = repaymentArrangementService.listRepaymentAccounts(invoice.agreementId)
            .first { it.repaymentAccount == repaymentInstruction.payerAccount }

        val invoiceArrangement = invoiceArrangementService.getOne(invoice.agreementId)
            ?: throw InvoiceArrangementNotFoundException("invoice arrangement not found")

        val loanAgreement =
            loanAgreementService.getOne(invoice.agreementId) ?: throw LoanAgreementNotFoundException("loan agreement not found")

        val actualRepayAmount =
            invoiceService.repayInvoice(repaymentInstruction.instructionAmount.toBigDecimal(), invoice, invoiceArrangement.graceDays ?: 0, tenantDateTime.now())
        actualRepayAmount[InvoiceAmountType.PRINCIPAL]?.run {
            if (this > BigDecimal.ZERO) {
                consumerLoanPublish.reducePositionKeeping(
                    DTOBankingTransaction(
                        name = repaymentAccount.repaymentAccountBank,
                        agreementId = invoice.agreementId,
                        instructionId = repaymentInstruction.id,
                        transactionDescription = null,
                        currency = repaymentInstruction.instructionCurrency,
                        amount = this,
                        businessUnit = repaymentInstruction.businessUnit,
                        appliedFee = null,
                        appliedRate = null,
                        customerId = loanAgreement.involvements.first { it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId,
                    )
                )
            }
        }

//        var firstFlag = true
//        invoiceList.filter { it.invoiceStatus != InvoiceStatus.FINISHED }
//            .sortedByDescending { it.invoiceRepaymentDate }
//            .forEach{
//                if(firstFlag) {
//                    it.invoiceStatus = InvoiceStatus.FINISHED
//                    invoiceService.save(it)
//                    firstFlag = false
//                }else{
//                    it.invoiceStatus = InvoiceStatus.CANCEL
//                    invoiceService.save(it)
//                    firstFlag = false
//                }
//            }
    }
}