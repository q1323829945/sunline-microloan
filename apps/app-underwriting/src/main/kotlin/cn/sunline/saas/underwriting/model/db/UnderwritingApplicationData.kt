package cn.sunline.saas.underwriting.model.db

/**
 * @title: DTOUnderwriting
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/8 16:31
 */
data class UnderwritingApplicationData (
    val applId:Long,
    val detail:DTODetail,
)

data class DTODetail(
    val customerId:Long,
    val name:String,
    val registrationNo:String,
)


