package cn.sunline.saas.underwriting.controllers.dto

/**
 * @title: DTOUnderwriting
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/8 16:31
 */
data class DTOCustomerCreditRating (
    val data:DTOCustomerCreditRatingData
)

data class DTOCustomerCreditRatingData(
    val applId:Long,
    val customerCreditRate:String,
)



