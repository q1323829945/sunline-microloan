package cn.sunline.saas.channel.rbac.modules.dto

data class DTOPositionAdd(
    val name:String,
    val remark:String? = null
)

data class DTOPositionChange(
    val id:String,
    val name:String,
    val remark: String? = null,
    val usersList:List<String>? = null
)

data class DTOPositionView(
    val id:String,
    val name: String,
    val remark: String? = null,
    val users:List<DTOPositionUserView>? = null
)

data class DTOPositionUserView(
    val id:Long,
    val username:String,
)