package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.constant.Frequency
import org.joda.time.DateTime


data class DTOCustomerStatistics(
    val personCount:Long,
    val organisationCount:Long,
    val partyCount:Long,
    val frequency: Frequency
)


data class DTOCustomerStatisticsFindParams(
    val datetime: DateTime,
    val frequency: Frequency,
)