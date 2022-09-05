package cn.sunline.saas.rpc.pubsub.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT_PUB_SUB
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.rpc.pubsub.ChannelPublish
import cn.sunline.saas.rpc.pubsub.ChannelPublishTopic
import cn.sunline.saas.rpc.pubsub.dto.DTOChannelData
import org.springframework.stereotype.Component

@Component
class ChannelPublishImpl: ChannelPublish {
    override fun syncChannel(dtoChannelData: DTOChannelData) {
        PubSubService.publish(
            pubSubName = APP_LOAN_MANAGEMENT_PUB_SUB,
            topic = ChannelPublishTopic.CHANNEL_SYNC.toString(),
            payload = dtoChannelData,
            tenant = ContextUtil.getTenant()
        )
    }
}