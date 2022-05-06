package cn.sunline.saas.rbac.controller.dto

import cn.sunline.saas.party.person.model.dto.DTOPersonView


data class DTOUserView(
    val id: String,
    val username: String,
    val email: String,
    val roles: List<DTOUserRoleView>,
    val person: DTOPersonView?
)