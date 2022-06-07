package cn.sunline.saas.statistics.modules.dto

import cn.sunline.saas.global.constant.PartyType
import java.util.*

data class DTOCustomerDetail(
    val partyId:Long,
    val partyType: PartyType,
)

data class DTOBusinessDetailQueryParams(
    val startDateTime: Date,
    val endDateTime: Date,
    val tenantId:Long? = null
)

data class DTOCustomerCount(
    val tenantId:Long,
    val personCount:Long,
    val organisationCount:Long,
    val partyCount:Long
)