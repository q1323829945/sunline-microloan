package cn.sunline.saas.customeroffer.service.dto

import cn.sunline.saas.customer.offer.modules.ApplyStatus

data class DTOCustomerOfferPage(
        var customerOfferId:String,
        var amount:String?,
        val datetime: Long,
        var productName:String,
        val status: ApplyStatus
)
