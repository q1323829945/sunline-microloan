package cn.sunline.saas.channel.rbac.modules.dto

data class DTOUserAdd(
    val username: String,
    val email: String,
    val password: String,
    val personId:String?,
    val positionId:String?
)

data class DTOUserChange(
    val email: String,
    val roleList: List<Long> = listOf(),
    val personId:String?,
    val positionId: String?
)

data class DTOUserView(
    val id: String,
    val username: String,
    val email: String,
    val roles: List<DTOUserRoleView>,
    var position: DTOUserPositionView?
)

data class DTOUserPositionView(
    val id:String?,
    val name:String?
)

class DTOUserRoleView (
    val id:String,
    val name: String
)