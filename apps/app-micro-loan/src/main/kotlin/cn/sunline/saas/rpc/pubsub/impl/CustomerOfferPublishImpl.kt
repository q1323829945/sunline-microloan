package cn.sunline.saas.rpc.pubsub.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.rpc.pubsub.CustomerOfferPublish
import cn.sunline.saas.rpc.pubsub.CustomerOfferPublishTopic
import cn.sunline.saas.rpc.pubsub.dto.DTOLoanApplicationData
import org.springframework.stereotype.Component

@Component
class CustomerOfferPublishImpl: CustomerOfferPublish {

    private val PUBSUB_NAME = "underwriting-pub-sub"

    override fun initiateUnderwriting(dtoLoanApplicationData: DTOLoanApplicationData) {

        PubSubService.publish(
            PUBSUB_NAME,
            CustomerOfferPublishTopic.INITIATE_UNDERWRITING.toString(),
            dtoLoanApplicationData
        )
    }

}