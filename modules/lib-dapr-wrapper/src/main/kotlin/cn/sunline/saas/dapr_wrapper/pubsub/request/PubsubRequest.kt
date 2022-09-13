package cn.sunline.saas.dapr_wrapper.pubsub.request

import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUUID
import cn.sunline.saas.global.util.getUserId

data class PubsubRequest(
    val data: Any,
) {
    val metadata: Map<String, String> = mapOf(
        Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
        Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
    )
}

data class BindingsRequest(
    val data: data1,
    val operation: String
) {
    val metadata: Map<String, String> = mapOf(
        Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant(),
        Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
    )
}

data class data1(
    val data: Any
)