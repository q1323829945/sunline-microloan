package cn.sunline.saas.request

import cn.sunline.saas.dapr_wrapper.constant.APP_GATEWAY
import cn.sunline.saas.dapr_wrapper.invoke.request.RPCRequest
import cn.sunline.saas.gateway.api.dto.ServerParams

class ServerRegisterRPCRequest(
    private val serverParams:ServerParams
): RPCRequest(serverParams.tenant) {

    override fun getQueryParams(): Map<String, String> {
        return mapOf()
    }

    override fun getHeaderParams(): Map<String, String> {
        return mapOf()
    }

    override fun getPayload(): Any {
        return serverParams
    }

    override fun getModuleName(): String {
        return APP_GATEWAY
    }

    override fun getMethodName(): String {
        return "/instance"
    }
}