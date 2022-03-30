package cn.sunline.saas.customer.offer.modules.dto

import cn.sunline.saas.customer.offer.modules.ApplyStatus

data class DTOCustomerOfferPage(
        var customerOfferId:Long,
        var amount:String?,
        val datetime: Long,
        var productName:String,
        val status: ApplyStatus
)
