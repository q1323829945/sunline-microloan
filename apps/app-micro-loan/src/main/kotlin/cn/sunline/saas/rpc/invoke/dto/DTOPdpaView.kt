package cn.sunline.saas.rpc.invoke.dto

data class DTOPdpaView (
    val id:String,
    var pdpaInformation:List<DTOPdpaItemView>? = null
)

data class DTOPdpaItemView(
    val item:String,
    val information: List<DTOPdpaInformationView>
)

data class DTOPdpaInformationView(
    val label:String,
    val name:String
)
