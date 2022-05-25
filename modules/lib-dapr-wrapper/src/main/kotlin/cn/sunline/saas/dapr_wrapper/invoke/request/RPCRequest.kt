package cn.sunline.saas.dapr_wrapper.invoke.request

abstract class RPCRequest(val tenant: String? = null) {

    abstract fun getQueryParams(): Map<String, String>
    abstract fun getHeaderParams(): Map<String, String>
    abstract fun getPayload(): Any?
    abstract fun getModuleName(): String
    abstract fun getMethodName(): String
}