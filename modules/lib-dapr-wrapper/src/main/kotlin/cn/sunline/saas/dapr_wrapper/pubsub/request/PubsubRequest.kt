package cn.sunline.saas.dapr_wrapper.pubsub.request

import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUserId

data class PubsubRequest(
    val data: Any,
) {
    val metadata: Map<String, String> = mapOf(
        Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
        Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
    )
}