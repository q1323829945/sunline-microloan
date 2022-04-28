package cn.sunline.saas.underwriting.db

/**
 * @title: DTOUnderwriting
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/8 16:31
 */
class UnderwritingApplicationData (
    val applId:Long,
    val detail: Detail,
)

class Detail(
    val customerId:Long,
    val name:String,
    val registrationNo:String,
)


