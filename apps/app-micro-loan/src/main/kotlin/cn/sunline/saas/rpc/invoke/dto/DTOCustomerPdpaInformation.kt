package cn.sunline.saas.rpc.invoke.dto

data class DTOCustomerPdpaInformation(
    val customerId:String,
    val pdpaId:String? = null,
    val electronicSignature:String? = null,
    val faceRecognition:String? = null,
    val fingerprint:String? = null,
)