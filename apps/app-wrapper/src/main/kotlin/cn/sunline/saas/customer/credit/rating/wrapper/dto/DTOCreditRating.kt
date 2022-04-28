package cn.sunline.saas.customer.credit.rating.wrapper.dto

data class DTOCreditRating(
    val applicationId:Long,
    val customerId: Long
)

data class DTOCallBackCustomerCreditRating(
    val applId:Long,
    val customerCreditRate:String
)