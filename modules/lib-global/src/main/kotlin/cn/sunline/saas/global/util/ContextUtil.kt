package cn.sunline.saas.global.util

import cn.sunline.saas.global.exception.TenantUninitializedException
import cn.sunline.saas.global.exception.UserUninitializedException
import org.joda.time.DateTimeZone

/**
 * @title: ContextUtil
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 16:31
 */
object ContextUtil {
    private val context = ThreadLocal<MutableMap<String, Any>>()

    internal const val TENANT_KEY = "TENANT_ID"
    internal const val USER_KEY = "USER_ID"
    internal const val TIME_ZONE = "TIME_ZONE"
    internal const val UUID_KEY = "UUID"

    internal fun put(key: String, value: Any) {
        if (context.get() == null) {
            context.set(mutableMapOf(key to value))
        } else {
            context.get()[key] = value
        }
    }

    internal fun get(key: String): Any? {
        return context.get()?.get(key)
    }

}

fun ContextUtil.setTenant(tenantId: String) {
    put(TENANT_KEY, tenantId)
}

fun ContextUtil.getTenant(): String {
    val tenantId = get(TENANT_KEY)
    if (tenantId == null){
        throw TenantUninitializedException("tenant is not in the context")
    }else{
        return tenantId.toString()
    }
}

fun ContextUtil.setUUID(tenantId: String) {
    put(UUID_KEY, tenantId)
}

fun ContextUtil.getUUID(): String {
    val tenantId = get(UUID_KEY)
    if (tenantId == null){
        throw TenantUninitializedException("tenant is not in the context")
    }else{
        return tenantId.toString()
    }
}

fun ContextUtil.setUserId(userId: String) {
    put(USER_KEY, userId)
}

fun ContextUtil.getUserId(): String {
    val userId = get(USER_KEY)
    if (userId == null){
        throw UserUninitializedException("user is not in the context")
    }else{
        return userId.toString()
    }
}

fun ContextUtil.getTimeZone(): DateTimeZone? {
    return get(TIME_ZONE)?.run { this as DateTimeZone }
}

fun ContextUtil.setTimeZone(timZone:DateTimeZone)  {
    put(TIME_ZONE, timZone)
}

fun ContextUtil.setApplyLoanSubmit(params:String){
    put("submit",params)
}

fun ContextUtil.getApplyLoanSubmit():Boolean{
    val submit = get("submit")
    return submit.toString() == "error"
}