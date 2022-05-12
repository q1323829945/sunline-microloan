package cn.sunline.saas.gateway.api.dto

data class DomainBindingParams(
    val groupId:String? = null,
    val urlDomain:String
)

data class DomainUnboundParams(
    val groupId:String? = null,
    val urlDomain:String
)

data class DomainResponseParams(
    val id:String
)

data class CertificateBindingParams(
    val groupId:String? = null,
    val name:String? = null,
    val certContent:String,
    val privateKey:String,
    val urlDomain:String
)

data class CertificateDeleteParams(
    val groupId:String,
    val urlDomain:String
)