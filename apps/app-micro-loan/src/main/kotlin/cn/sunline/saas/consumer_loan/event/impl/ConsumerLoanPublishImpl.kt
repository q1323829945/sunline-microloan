package cn.sunline.saas.consumer_loan.event.impl

import cn.sunline.saas.banking.transaction.model.dto.DTOBankingTransaction
import cn.sunline.saas.consumer_loan.event.ConsumerLoanPublish
import cn.sunline.saas.consumer_loan.event.ConsumerLoanPublishTopic
import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.disbursement.instruction.model.dto.DTODisbursementInstruction
import cn.sunline.saas.dapr_wrapper.constant.APP_MICRO_LOAN_PUB_SUB
import cn.sunline.saas.repayment.instruction.model.dto.DTORepaymentInstruction
import org.springframework.stereotype.Component

@Component
class ConsumerLoanPublishImpl: ConsumerLoanPublish {
    override fun initiatePositionKeeping(dtoBankingTransaction: DTOBankingTransaction) {
        PubSubService.publish(
            APP_MICRO_LOAN_PUB_SUB,
            ConsumerLoanPublishTopic.INITIATE_POSITION_KEEPING.toString(),
            dtoBankingTransaction
        )
    }

    override fun reducePositionKeeping(dtoBankingTransaction: DTOBankingTransaction) {
        PubSubService.publish(
            APP_MICRO_LOAN_PUB_SUB,
            ConsumerLoanPublishTopic.REDUCE_POSITION_KEEPING.toString(),
            dtoBankingTransaction
        )
    }

    override fun financialAccounting(dtoBankingTransaction: DTODisbursementInstruction) {
    }

    override fun disbursement(dtoBankingTransaction: DTODisbursementInstruction) {
    }

    override fun financialAccountingRepayment(dtoRepaymentInstruction: DTORepaymentInstruction){
    }

    override fun repayment(dtoRepaymentInstruction: DTORepaymentInstruction) {
    }

    override fun financialAccountingPrepayment(dtoRepaymentInstruction: DTORepaymentInstruction){
    }

    override fun prepayment(dtoRepaymentInstruction: DTORepaymentInstruction) {
    }

    override fun feePayment(dtoRepaymentInstruction: DTORepaymentInstruction){

    }
}