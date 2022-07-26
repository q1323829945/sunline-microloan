package cn.sunline.saas.test.steps.dto


data class DTOCustomerPdpaInformation(
    val customerId:String,
    val pdpaId:String? = null,
    val electronicSignature:String? = null,
    val faceRecognition:String? = null,
    val fingerprint:String? = null,
)

data class DTOCustomerPdpaInformationChange(
    val customerId: String,
    val pdpaId:String? = null,
    val electronicSignature:String? = null,
    val faceRecognition:String? = null,
    val fingerprint:String? = null,
)

data class DTOPdpaAdd (
    val country: String,
    val language: String,
    val pdpaInformation:List<DTOPdpaItem>
)

data class DTOPdpaChange (
    val pdpaInformation:List<DTOPdpaItem>
)

data class DTOPdpaView (
    val id:String,
    val country: String,
    val language: String,
    var pdpaInformation:List<DTOPdpaItem>? = null
)

data class DTOPdpaItem(
    val item:String,
    val information: List<DTOPdpaInformation>
)

data class DTOPdpaInformation(
    val label:String,
    val name:String
)

data class DTOPdpaAuthority(
    var isElectronicSignature: Boolean = false,
    var isFaceRecognition : Boolean = false,
    var isFingerprint: Boolean = false,
)