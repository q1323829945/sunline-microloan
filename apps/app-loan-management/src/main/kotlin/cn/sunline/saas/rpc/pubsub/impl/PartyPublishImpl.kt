package cn.sunline.saas.rpc.pubsub.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT_PUB_SUB
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.rpc.pubsub.PartyPublish
import cn.sunline.saas.rpc.pubsub.PartyPublishTopic
import cn.sunline.saas.rpc.pubsub.dto.DTOCustomerDetail
import org.springframework.stereotype.Component

@Component
class PartyPublishImpl:PartyPublish {
    override fun addCustomerDetail(dtoCustomerDetail: DTOCustomerDetail) {
        PubSubService.publish(
            pubSubName = APP_LOAN_MANAGEMENT_PUB_SUB,
            topic = PartyPublishTopic.CUSTOMER_DETAIL.toString(),
            payload = dtoCustomerDetail,
            tenant = ContextUtil.getTenant()
        )
    }
}