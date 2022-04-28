package cn.sunline.saas.underwriting.event

import cn.sunline.saas.underwriting.db.Underwriting

interface UnderwritingBinding {

    fun retrieveCustomerCreditRating(applicationId:Long,partner: String, customerId: Long)

    fun execCreditRisk(partner:String,underwriting: Underwriting)

    fun execRegulatoryCompliance(partner: String, underwriting: Underwriting)

    fun execFraudEvaluation(partner: String, underwriting: Underwriting)
}