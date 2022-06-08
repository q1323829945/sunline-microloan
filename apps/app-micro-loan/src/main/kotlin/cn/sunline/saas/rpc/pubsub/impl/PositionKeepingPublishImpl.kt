package cn.sunline.saas.rpc.pubsub.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.rpc.pubsub.PositionKeepingPublish
import cn.sunline.saas.rpc.pubsub.PositionKeepingPublishTopic
import cn.sunline.saas.rpc.pubsub.dto.DTOBusinessDetail
import org.springframework.stereotype.Component

@Component
class PositionKeepingPublishImpl:PositionKeepingPublish {
    override fun addBusinessDetail(dtoBusinessDetail: DTOBusinessDetail) {
        PubSubService.publish(
            pubSubName = "underwriting-pub-sub",
            topic = PositionKeepingPublishTopic.BUSINESS_DETAIL.toString(),
            payload = dtoBusinessDetail
        )
    }
}