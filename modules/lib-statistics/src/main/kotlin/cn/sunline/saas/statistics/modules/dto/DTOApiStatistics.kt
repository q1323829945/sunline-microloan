package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.constant.Frequency


data class DTOApiStatistics(
    val api: String,
    val count:Long,
    val frequency: Frequency
)