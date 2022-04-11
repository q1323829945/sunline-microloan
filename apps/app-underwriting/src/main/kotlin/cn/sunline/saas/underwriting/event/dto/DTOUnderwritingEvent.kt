package cn.sunline.saas.underwriting.event

/**
 * @title: DTOUnderwritingEvent
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/7 16:06
 */
data class DTORetrieveCustomerCreditRating(
    val partner: String,
    val customerId: Long
)
