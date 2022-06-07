package cn.sunline.saas.statistics.modules.dto

import java.util.Date

data class DTOApiDetailQueryDate(
    val startDateTime:Date,
    val endDateTime:Date,
    val tenantId:Long? = null
)


data class DTOApiDetailApiCount(
    val api:String,
    val count:Long
)
