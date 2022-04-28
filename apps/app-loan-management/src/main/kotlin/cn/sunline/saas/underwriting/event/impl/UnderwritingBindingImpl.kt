package cn.sunline.saas.underwriting.event.impl

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.underwriting.event.UnderwritingBinding
import cn.sunline.saas.underwriting.event.UnderwritingPublishTopic
import cn.sunline.saas.underwriting.event.dto.DTOExecCreditRisk
import cn.sunline.saas.underwriting.event.dto.DTOExecFraudEvaluation
import cn.sunline.saas.underwriting.event.dto.DTOExecRegulatoryCompliance
import cn.sunline.saas.underwriting.event.dto.DTORetrieveCustomerCreditRating
import cn.sunline.saas.underwriting.db.Underwriting
import org.springframework.stereotype.Component

@Component
class UnderwritingBindingImpl: UnderwritingBinding {

    private val BINDING_OPERATION = "create"

    override fun retrieveCustomerCreditRating(applicationId: Long, partner: String, customerId: Long) {
        val dtoRetrieveCustomerCreditRating = DTORetrieveCustomerCreditRating(applicationId, partner, customerId)
        DaprHelper.binding(
            UnderwritingPublishTopic.RETRIEVE_CUSTOMER_CREDIT_RATING.toString(),
            BINDING_OPERATION,
            dtoRetrieveCustomerCreditRating
        )
    }

    override fun execCreditRisk(partner: String, underwriting: Underwriting) {
        val dtoExecCreditRisk = DTOExecCreditRisk(
            underwriting.id, partner,
            underwriting.customerCreditRate!!
        )

        DaprHelper.binding(
            UnderwritingPublishTopic.EXECUTE_CREDIT_RISK.toString(),
            BINDING_OPERATION,
            dtoExecCreditRisk
        )
    }

    override fun execRegulatoryCompliance(partner: String, underwriting: Underwriting) {
        val dTOExecRegulatoryCompliance = DTOExecRegulatoryCompliance(
            underwriting.id,partner,
            underwriting.creditRisk!!
        )

        DaprHelper.binding(
            UnderwritingPublishTopic.EXECUTE_REGULATORY_COMPLIANCE.toString(),
            BINDING_OPERATION,
            dTOExecRegulatoryCompliance
        )
    }

    override fun execFraudEvaluation(partner: String, underwriting: Underwriting) {
        val dtoExecFraudEvaluation = DTOExecFraudEvaluation(
            underwriting.id,partner,
            underwriting.fraudEvaluation!!
        )
        DaprHelper.binding(
            UnderwritingPublishTopic.FRAUD_EVALUATION.toString(),
            BINDING_OPERATION,
            dtoExecFraudEvaluation
        )
    }
}