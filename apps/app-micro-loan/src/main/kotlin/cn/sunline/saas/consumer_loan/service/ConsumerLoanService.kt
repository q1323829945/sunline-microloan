package cn.sunline.saas.consumer_loan.service

import cn.sunline.saas.consumer.loan.event.ConsumerLoanPublish
import cn.sunline.saas.consumer.loan.exception.DisbursementArrangementNotFoundException
import cn.sunline.saas.consumer.loan.exception.LoanAgreementNotFoundException
import cn.sunline.saas.consumer.loan.exception.LoanAgreementStatusCheckException
import cn.sunline.saas.rpc.consumerloan.ConsumerLoanInvoke
import cn.sunline.saas.consumer.loan.service.assembly.ConsumerLoanAssemblyimport cn.sunline.saas.disbursement.arrangement.service.DisbursementArrangementService
import cn.sunline.saas.disbursement.instruction.model.dto.DTODisbursementInstructionAdd
import cn.sunline.saas.disbursement.instruction.service.DisbursementInstructionService
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.loan.agreement.model.LoanAgreementInvolvementType
import cn.sunline.saas.loan.agreement.model.db.LoanAgreement
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.underwriting.arrangement.service.UnderwritingArrangementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: ConsumerLoanService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 14:12
 */
@Service
class ConsumerLoanService(
    private val consumerLoanInvoke: ConsumerLoanInvoke,
    private val consumerLoanPublish: ConsumerLoanPublish
) {

    @Autowired
    private lateinit var loanAgreementService: LoanAgreementService

    @Autowired
    private lateinit var underwritingArrangementService: UnderwritingArrangementService

    @Autowired
    private lateinit var disbursementArrangementService: DisbursementArrangementService

    @Autowired
    private lateinit var disbursementInstructionService: DisbursementInstructionService

    fun createLoanAgreement(applicationId: Long) {
        val customerOffer = consumerLoanInvoke.retrieveCustomerOffer(applicationId)
        val loanProduct = consumerLoanInvoke.retrieveLoanProduct(customerOffer.productId)

        val loanAgreementAggregate = loanAgreementService.registered(
            ConsumerLoanAssembly.convertToDTOLoanAgreementAdd(
                customerOffer, loanProduct
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

        val loanAgreement = loanAgreementService.archiveAgreement(loanAgreementAggregate)
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

        consumerLoanPublish.initiatePositionKeeping(disbursementInstruction)
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
}