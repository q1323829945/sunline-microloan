package cn.sunline.saas.huaweicloud.apig.constant

data class DomainBindingParams(
    val group_id:String,
    val url_domain:String
)

data class DomainUnboundParams(
    val group_id:String,
    val url_domain:String
)


data class CertificateBindingParams(
    val group_id:String,
    val name:String,
    val cert_content:String,
    val private_key:String,
    val url_domain:String
)

data class CertificateDeleteParams(
    val group_id:String,
    val url_domain:String
)