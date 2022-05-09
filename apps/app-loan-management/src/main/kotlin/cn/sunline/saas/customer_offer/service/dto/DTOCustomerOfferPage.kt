package cn.sunline.saas.customer_offer.service.dto

import cn.sunline.saas.customer.offer.modules.ApplyStatus

data class DTOCustomerOfferPage(
    val customerOfferId:Long,
    val legalEntityIndicator: String?,
    val organisationSector:String?,
    val amount:String?,
    val datetime: String,
    val productName:String,
    val status: ApplyStatus,
)
