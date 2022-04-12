package cn.sunline.saas.underwriting.event

/**
 * @title: UnderwritingPublish
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 11:17
 */
interface UnderwritingPublish {

    fun retrieveCustomerCreditRating(partner: String, customerId: Long)
}