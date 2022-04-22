package cn.sunline.saas.consumer.loan.dto

data class DTOLoanApplication (
    var applId:Long? = null,
    val detail: DTODetail,
)

data class DTODetail(
    val customerId:Long,
    val name:String,
    val registrationNo:String,
)
