package cn.sunline.saas.customer_offer.service.dto

import cn.sunline.saas.customer.offer.modules.ApplyStatus

data class DTOCustomerOfferPage(
        var customerOfferId:String,
        var amount:String?,
        val datetime: Long,
        var productName:String,
        val status: ApplyStatus
)
