package cn.sunline.saas.modules.dto


data class DTOServer (
    val tenant:String,
    val server:String,
    val domain: String,
    val enabled:Boolean = true,
)