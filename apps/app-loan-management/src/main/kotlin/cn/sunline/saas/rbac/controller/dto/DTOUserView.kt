package cn.sunline.saas.rbac.controller.dto

import cn.sunline.saas.rbac.service.dto.DTOPerson


data class DTOUserView(
    val id: String,
    val username: String,
    val email: String,
    val roles: List<DTOUserRoleView>,
    val person: DTOPerson?
)