package cn.sunline.saas.modules.dto

data class DTOStatistics (
    val tenant:String,
    val server:String,
    val method:String,
    val path:String,
    val query:String? = null,
)
