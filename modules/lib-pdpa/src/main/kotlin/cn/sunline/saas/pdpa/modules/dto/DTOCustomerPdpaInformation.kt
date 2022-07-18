package cn.sunline.saas.pdpa.modules.dto

data class DTOCustomerPdpaInformation(
    val customerId:String,
    val pdpaId:String? = null,
    val electronicSignature:String? = null,
    val faceRecognition:String? = null,
    val fingerprint:String? = null,
)

data class DTOCustomerPdpaInformationChange(
    val pdpaId:String? = null,
    val electronicSignature:String? = null,
    val faceRecognition:String? = null,
    val fingerprint:String? = null,
)