package cn.sunline.saas.rpc.pubsub.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT_PUB_SUB
import cn.sunline.saas.rpc.pubsub.PdpaPublish
import cn.sunline.saas.rpc.pubsub.PdpaPublishTopic
import org.springframework.stereotype.Component

@Component
class PdpaPublishImpl:PdpaPublish {
    data class Withdraw(
        val id:String
    )

    override fun customerPdpaConfirm(customerId: String) {
        PubSubService.publish(
            pubSubName = APP_LOAN_MANAGEMENT_PUB_SUB,
            topic = PdpaPublishTopic.CUSTOMER_PDPA_CONFIRM.toString(),
            payload = customerId
        )
    }

    override fun customerPdpaWithdraw(customerId: String) {
        PubSubService.publish(
            pubSubName = APP_LOAN_MANAGEMENT_PUB_SUB,
            topic = PdpaPublishTopic.CUSTOMER_PDPA_WITHDRAW.toString(),
            payload = Withdraw(customerId)
        )
    }
}