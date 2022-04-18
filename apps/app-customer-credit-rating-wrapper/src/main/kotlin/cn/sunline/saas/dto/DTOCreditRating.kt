package cn.sunline.saas.dto

data class DTOCreditRating(
    val data:DTOCreditRatingData,
)

data class DTOCreditRatingData(
    val applicationId:Long,
    val partner: String,
    val customerId: Long
)

data class DTOCallBackCustomerCreditRating(
    val applId:Long,
    val customerCreditRate:String
)