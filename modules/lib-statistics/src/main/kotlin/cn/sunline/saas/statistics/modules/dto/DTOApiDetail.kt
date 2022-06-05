package cn.sunline.saas.statistics.modules.dto

import java.util.Date

data class DTOApiDetail(
    val id:String,
    val api: String
)


data class DTOApiDetailQueryCountParams(
    val api:String,
    val startDateTime:Date,
    val endDateTime:Date
)
