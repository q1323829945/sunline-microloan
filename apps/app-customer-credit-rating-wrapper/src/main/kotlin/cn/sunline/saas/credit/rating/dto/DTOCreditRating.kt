package cn.sunline.saas.credit.rating.dto

data class DTOCreditRating(
    val applicationId:Long,
    val partner: String,
    val customerId: Long
)

data class DTOCallBackCustomerCreditRating(
    val applId:Long,
    val customerCreditRate:String
)