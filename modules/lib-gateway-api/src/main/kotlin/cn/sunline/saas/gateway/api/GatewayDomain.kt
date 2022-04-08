package cn.sunline.saas.gateway.api

interface GatewayDomain {
    fun domainBinding(domainParams:Any):Any?

    fun domainUnbound(domainParams:Any)

    fun certificateBinding(certificateBindingParams:Any)

    fun certificateDelete(certificateDeleteParams:Any)
}