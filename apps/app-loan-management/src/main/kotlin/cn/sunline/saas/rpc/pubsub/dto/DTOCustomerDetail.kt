package cn.sunline.saas.rpc.pubsub.dto

import cn.sunline.saas.global.constant.PartyType

data class DTOCustomerDetail(
    val partyId:Long,
    val partyType: PartyType
)
