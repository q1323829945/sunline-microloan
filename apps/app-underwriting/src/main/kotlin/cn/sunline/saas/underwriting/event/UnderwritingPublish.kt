package cn.sunline.saas.underwriting.event

import cn.sunline.saas.underwriting.model.db.Underwriting

/**
 * @title: UnderwritingPublish
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 11:17
 */
interface UnderwritingPublish {

    fun retrieveCustomerCreditRating(applicationId:Long,partner: String, customerId: Long)

    fun execCreditRisk(partner:String,underwriting: Underwriting)

    fun execRegulatoryCompliance(partner: String, underwriting: Underwriting)

    fun execFraudEvaluation(partner: String, underwriting: Underwriting)
}