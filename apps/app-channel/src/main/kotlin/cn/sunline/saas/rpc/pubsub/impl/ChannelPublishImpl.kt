package cn.sunline.saas.rpc.pubsub.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.dapr_wrapper.pubsub.request.PubsubRequest
import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT_PUB_SUB
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT_PUB_SUB_KAFKA
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT_PUB_SUB_RABBITMQ
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.rpc.pubsub.ChannelPublish
import cn.sunline.saas.rpc.pubsub.ChannelPublishTopic
import cn.sunline.saas.rpc.pubsub.dto.DTOChannelData
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import java.util.*

@Component
class ChannelPublishImpl: ChannelPublish {
    override fun syncChannel(dtoChannelData: DTOChannelData) {
        PubSubService.bindings(
            pubSubName = APP_LOAN_MANAGEMENT_PUB_SUB_KAFKA,
            topic = ChannelPublishTopic.CHANNEL_SYNC.toString(),
            payload = dtoChannelData,
            tenant = ContextUtil.getTenant()
        )
    }
}