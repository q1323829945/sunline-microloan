package cn.sunline.saas.customer_offer.service.dto

import cn.sunline.saas.global.constant.ApplyStatus

data class DTOCustomerOfferProcedure(
        var customerOfferId:String,
        var amount:String?,
        val datetime: String,
        var productName:String,
        val status: ApplyStatus
)

data class DTOProductUploadConfig(
        val id:String,
        val name:String
)




