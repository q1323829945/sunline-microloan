package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.constant.Frequency


data class DTOCustomerStatistics(
    val tenantId: Long,
    val personCount:Long,
    val organisationCount:Long,
    val partyCount:Long,
    val frequency: Frequency
)