package cn.sunline.saas.consumer_loan.event.impl

import cn.sunline.saas.banking.transaction.model.dto.DTOBankingTransaction
import cn.sunline.saas.consumer_loan.event.ConsumerLoanPublish
import cn.sunline.saas.consumer_loan.event.ConsumerLoanPublishTopic
import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.disbursement.instruction.model.dto.DTODisbursementInstruction
import org.springframework.stereotype.Component

@Component
class ConsumerLoanPublishImpl: ConsumerLoanPublish {

    private val PUBSUB_NAME = "underwriting-pub-sub"

    override fun initiatePositionKeeping(dtoBankingTransaction: DTOBankingTransaction) {
        PubSubService.publish(
            PUBSUB_NAME,
            ConsumerLoanPublishTopic.INITIATE_POSITION_KEEPING.toString(),
            dtoBankingTransaction
        )
    }

    override fun financialAccounting(dtoBankingTransaction: DTODisbursementInstruction) {
    }

    override fun disbursement(dtoBankingTransaction: DTODisbursementInstruction) {
    }
}