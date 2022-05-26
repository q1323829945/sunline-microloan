package cn.sunline.saas.underwriting.event.dto

import cn.sunline.saas.underwriting.db.UnderwritingType

data class DTOCustomerOffer (
    val id:Long,
    val underwritingType: UnderwritingType,
)