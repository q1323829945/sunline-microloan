package cn.sunline.saas.request

import cn.sunline.saas.dapr_wrapper.constant.APP_GATEWAY
import cn.sunline.saas.dapr_wrapper.invoke.request.RPCRequest

class ServerRemoveRPCRequest(private val client:String): RPCRequest(client) {

    override fun getQueryParams(): Map<String, String> {
        return mapOf()
    }

    override fun getHeaderParams(): Map<String, String> {
        return mapOf()
    }

    override fun getPayload(): Any? {
        return null
    }

    override fun getModuleName(): String {
        return APP_GATEWAY
    }

    override fun getMethodName(): String {
        return "/instance/${client}"
    }
}