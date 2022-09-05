package cn.sunline.saas.channel.statistics.modules.dto

import cn.sunline.saas.global.constant.PartyType
import java.util.*

data class DTOCustomerDetail(
    val partyId:Long,
    val partyType: PartyType,
)

data class DTOCustomerDetailQueryParams(
    val startDateTime: Date,
    val endDateTime: Date,
)

data class DTOCustomerCount(
    val tenantId:Long,
    val personCount:Long,
    val organisationCount:Long,
    val partyCount:Long
)