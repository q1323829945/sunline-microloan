package cn.sunline.saas.gateway.api

import cn.sunline.saas.gateway.api.dto.*

interface GatewayDomain {
    fun domainBinding(domainParams: DomainBindingParams): DomainResponseParams

    fun domainUnbound(domainParams: DomainUnboundParams)

    fun certificateBinding(certificateBindingParams: CertificateBindingParams)

    fun certificateDelete(certificateDeleteParams: CertificateDeleteParams)
}