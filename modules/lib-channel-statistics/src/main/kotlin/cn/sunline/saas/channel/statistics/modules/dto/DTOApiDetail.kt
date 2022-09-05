package cn.sunline.saas.channel.statistics.modules.dto

import java.util.Date

data class DTOApiDetailQueryParams(
    val startDateTime:Date,
    val endDateTime:Date,
)


data class DTOApiCount(
    val api:String,
    val count:Long
)
