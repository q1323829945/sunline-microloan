package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.constant.Frequency
import org.joda.time.DateTime


data class DTOApiStatistics(
    val api: String,
    val count:Long,
    val frequency: Frequency
)


data class DTOApiStatisticsFindParams(
    val api:String,
    val datetime:DateTime,
    val frequency: Frequency,
)