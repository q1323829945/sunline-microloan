package cn.sunline.saas.huaweicloud.apig.constant

data class HuaweiCloudDomainBindingParams(
    val group_id:String,
    val url_domain:String
)

data class HuaweiCloudDomainUnboundParams(
    val group_id:String,
    val url_domain:String
)


data class HuaweiCloudCertificateBindingParams(
    val group_id:String,
    val name:String,
    val cert_content:String,
    val private_key:String
)

data class HuaweiCloudCertificateDeleteParams(
    val group_id:String,
    val url_domain:String
)