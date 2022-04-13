package cn.sunline.saas.underwriting.event.dto

/**
 * @title: DTOCreditRisk
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/12 15:21
 */
data class DTOExecCreditRisk(
    val applicationId: Long,
    val partner: String,
    val customerCreditRating: String
)
