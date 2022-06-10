package cn.sunline.saas.tenant.service.dto

data class DTOTenant(
    val id:Long,
    val country: String,
    val enabled:Boolean?,
    val permissions:List<DTOTenantPermission>?
)


data class DTOTenantPermission(
    val productApplicationId:String,
    val subscriptionId:Long? = null
)
