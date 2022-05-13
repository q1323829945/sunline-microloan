package cn.sunline.saas.underwriting.controllers.dto

/**
 * @title: DTOUnderwriting
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/8 16:31
 */
data class DTOLoanApplicationData (
    val applId:String,
    val detail:DTODetail,
)

data class DTODetail(
    val customerId:String,
    val name:String,
    val registrationNo:String,
)


