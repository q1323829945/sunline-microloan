package cn.sunline.saas.tenant.service.dto

import java.util.UUID

data class DTOTenant(
    val id:Long,
    val name:String,
    val country: String,
    val enabled:Boolean,
    val saasUUID:String,
)
