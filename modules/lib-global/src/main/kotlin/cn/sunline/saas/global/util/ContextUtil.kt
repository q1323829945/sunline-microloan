package cn.sunline.saas.global.util

import cn.sunline.saas.global.exception.ContextNullException

/**
 * @title: ContextUtil
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 16:31
 */
object ContextUtil {
    private val context = ThreadLocal<MutableMap<String, String>>()

    internal const val TENANT_KEY = "TENANT_ID"
    internal const val REQUEST_KEY = "REQUEST_ID"
    internal const val USER_KEY = "USER_ID"

    internal fun put(key: String, value: String) {
        if (context.get() == null) {
            context.set(mutableMapOf(key to value))
        } else {
            context.get()[key] = value
        }
    }

    internal fun get(key: String): String? {
        return context.get()?.get(key)
    }

}

fun ContextUtil.setTenant(tenantId: String) {
    put(TENANT_KEY, tenantId)
}

fun ContextUtil.getTenant(): Long {
    return get(TENANT_KEY)?.toLong() ?: throw ContextNullException("tenant id is null in context")
}

fun ContextUtil.setRequestId(requestId: String) {
    put(REQUEST_KEY, requestId)
}

fun ContextUtil.getRequestId(): String {
    return get(REQUEST_KEY)?: throw ContextNullException("request id is null in context")
}

fun ContextUtil.setUserId(userId: String) {
    put(USER_KEY, userId)
}

fun ContextUtil.getUserId(): String {
    return get(USER_KEY)?: throw ContextNullException("user id is null in context")
}