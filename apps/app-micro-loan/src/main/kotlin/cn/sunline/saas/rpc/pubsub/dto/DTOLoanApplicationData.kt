package cn.sunline.saas.rpc.pubsub.dto

data class DTOLoanApplicationData (
    val applId:String,
    val detail:DTODetail,
)

data class DTODetail(
    val customerId:String,
    val name:String,
    val registrationNo:String,
)
