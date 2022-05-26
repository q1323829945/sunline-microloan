package cn.sunline.saas.consumer_loan.service

import cn.sunline.saas.banking.transaction.model.dto.DTOBankingTransaction
import cn.sunline.saas.consumer_loan.event.ConsumerLoanPublish
import cn.sunline.saas.consumer_loan.exception.DisbursementArrangementNotFoundException
import cn.sunline.saas.loan.agreement.exception.LoanAgreementNotFoundException
import cn.sunline.saas.consumer_loan.exception.LoanAgreementStatusCheckException
import cn.sunline.saas.consumer_loan.invoke.ConsumerLoanInvoke
import cn.sunline.saas.consumer_loan.service.assembly.ConsumerLoanAssembly
import cn.sunline.saas.consumer_loan.service.dto.DTOLoanAgreementView
import cn.sunline.saas.disbursement.arrangement.service.DisbursementArrangementService
import cn.sunline.saas.disbursement.instruction.model.dto.DTODisbursementInstructionAdd
import cn.sunline.saas.disbursement.instruction.service.DisbursementInstructionService
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.interest.arrangement.component.getExecutionRate
import cn.sunline.saas.interest.component.InterestRateHelper
import cn.sunline.saas.invoice.service.InvoiceService
import cn.sunline.saas.loan.agreement.model.LoanAgreementInvolvementType
import cn.sunline.saas.loan.agreement.model.db.LoanAgreement
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.schedule.ScheduleService
import cn.sunline.saas.underwriting.arrangement.service.UnderwritingArrangementService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

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


    fun createLoanAgreement(applicationId: Long) {
        val customerOffer = consumerLoanInvoke.retrieveCustomerOffer(applicationId)
        val loanProduct = consumerLoanInvoke.retrieveLoanProduct(customerOffer.productId)
        val loanAgreementAggregate = loanAgreementService.registered(
            ConsumerLoanAssembly.convertToDTOLoanAgreementAdd(
                customerOffer,
                loanProduct,
                InterestRateHelper.getRate(customerOffer.term, consumerLoanInvoke.retrieveBaseInterestRate(loanProduct.interestFeature.id))?.toPlainString()
            )
        )

        customerOffer.guarantors?.run {
            underwritingArrangementService.registered(
                ConsumerLoanAssembly.convertToDTOUnderwritingArrangementAdd(
                    loanAgreementAggregate,
                    this
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
            loanProduct.interestFeature.interest.baseYearDays        ).getSchedules(loanProduct.repaymentFeature.payment.paymentMethod)

        invoiceService.initiateLoanInvoice(ConsumerLoanAssembly.convertToDTOLoanInvoice(schedules,loanAgreementAggregate))

        val loanAgreement = loanAgreementService.archiveAgreement(loanAgreementAggregate.loanAgreement)
        signAndLending(loanAgreement)

    }

    fun signLoanAgreementByOffline(agreementId: Long) {
        val loanAgreement = loanAgreementService.getOne(agreementId)
            ?: throw LoanAgreementNotFoundException("loan agreement not found")
        signAndLending(loanAgreement)
    }

    private fun signAndLending(loanAgreement: LoanAgreement) {
        val signLoanAgreement = loanAgreementService.signAgreement(loanAgreement)
        lending(signLoanAgreement.id)
    }

    private fun lending(agreementId: Long) {
        val loanAgreement = loanAgreementService.getOne(agreementId)
            ?: throw LoanAgreementNotFoundException("loan agreement not found")
        if (loanAgreement.status != AgreementStatus.SIGNED) {
            throw LoanAgreementStatusCheckException("loan agreement hasn't signed")
        }
        val disbursementArrangement = disbursementArrangementService.getOne(loanAgreement.id)
            ?: throw DisbursementArrangementNotFoundException("disbursement arrangement not found")

        val dtoDisbursementInstruction = DTODisbursementInstructionAdd(
            moneyTransferInstructionAmount = disbursementArrangementService.calculateLendingAmount(
                loanAgreement.amount,
                disbursementArrangement
            ),
            moneyTransferInstructionCurrency = loanAgreement.currency,
            moneyTransferInstructionPurpose = loanAgreement.purpose,
            payeeAccount = disbursementArrangement.disbursementAccount,
            payerAccount = null,
            agreementId = agreementId,
            businessUnit = loanAgreement.involvements.first { LoanAgreementInvolvementType.LOAN_LENDER == it.involvementType }.partyId
        )

        val disbursementInstruction = disbursementInstructionService.registered(dtoDisbursementInstruction)

        consumerLoanPublish.initiatePositionKeeping(DTOBankingTransaction(
            name = disbursementArrangement.disbursementAccountBank,
            agreementId = disbursementInstruction.agreementId,
            instructionId = disbursementInstruction.id,
            transactionDescription = null,
            currency = loanAgreement.currency,
            amount = loanAgreement.amount,
            businessUnit = disbursementInstruction.businessUnit,
            appliedFee = null,
            appliedRate = null,
            customerId = loanAgreement.involvements.first{ it.involvementType == LoanAgreementInvolvementType.LOAN_BORROWER }.partyId,
        ))
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

    fun getLoanAgreementByApplicationId(applicationId:Long):DTOLoanAgreementView?{
        val loanAgreement = loanAgreementService.findByApplicationId(applicationId)
        return loanAgreement?.run { objectMapper.convertValue(loanAgreement) }
    }

    fun updateLoanAgreementStatus(applicationId: Long,status: AgreementStatus){
        val  loanAgreement = loanAgreementService.findByApplicationId(applicationId) ?: throw LoanAgreementNotFoundException("loan agreement not found")
        loanAgreement.status = status
        loanAgreementService.save(loanAgreement)
    }
}