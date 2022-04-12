package cn.sunline.saas.underwriting.event.dto

/**
 * @title: DTOUnderwritingEvent
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/7 16:06
 */
data class DTORetrieveCustomerCreditRating(
    val applicationId:Long,
    val partner: String,
    val customerId: Long
)
