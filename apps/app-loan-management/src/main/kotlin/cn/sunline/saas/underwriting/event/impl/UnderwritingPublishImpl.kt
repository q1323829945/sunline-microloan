package cn.sunline.saas.underwriting.event.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.global.constant.*
import cn.sunline.saas.underwriting.event.UnderwritingPublish
import cn.sunline.saas.underwriting.event.UnderwritingPublishTopic
import cn.sunline.saas.underwriting.db.Underwriting
import cn.sunline.saas.underwriting.event.dto.*
import org.springframework.stereotype.Component

/**
 * @title: UnderwritingPublish
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/7 15:41
 */
@Component
class UnderwritingPublishImpl : UnderwritingPublish {

    override fun retrieveCustomerCreditRating(applicationId: Long, partner: String, customerId: Long) {
        val dtoRetrieveCustomerCreditRating = DTORetrieveCustomerCreditRating(applicationId, partner, customerId)
        PubSubService.publish(
            APP_WRAPPER_PUB_SUB,
            UnderwritingPublishTopic.RETRIEVE_CUSTOMER_CREDIT_RATING.toString(),
            dtoRetrieveCustomerCreditRating,
        )
    }

    override fun execCreditRisk(partner: String, underwriting: Underwriting) {
        val dtoExecCreditRisk = DTOExecCreditRisk(
            underwriting.id, partner,
            underwriting.customerCreditRate!!
        )

        PubSubService.publish(
            APP_WRAPPER_PUB_SUB,
            UnderwritingPublishTopic.EXECUTE_CREDIT_RISK.toString(),
            dtoExecCreditRisk,
        )
    }

    override fun execRegulatoryCompliance(partner: String, underwriting: Underwriting) {
        val dTOExecRegulatoryCompliance = DTOExecRegulatoryCompliance(
            underwriting.id,partner,
            underwriting.creditRisk!!
        )

        PubSubService.publish(
            APP_WRAPPER_PUB_SUB,
            UnderwritingPublishTopic.EXECUTE_REGULATORY_COMPLIANCE.toString(),
            dTOExecRegulatoryCompliance,
        )
    }

    override fun execFraudEvaluation(partner: String, underwriting: Underwriting) {
        val dtoExecFraudEvaluation = DTOExecFraudEvaluation(
            underwriting.id,partner,
            underwriting.fraudEvaluation!!
        )


        PubSubService.publish(
            APP_WRAPPER_PUB_SUB,
            UnderwritingPublishTopic.FRAUD_EVALUATION.toString(),
            dtoExecFraudEvaluation,
        )
    }


    override fun initiateLoanAgreement(applicationId: Long) {
        PubSubService.publish(
            APP_MICRO_LOAN_PUB_SUB,
            UnderwritingPublishTopic.INITIATE_LOAN_AGREEMENT.toString(),
            applicationId
        )
    }

    override fun customerOfferApproval(applicationId: Long) {
        PubSubService.publish(
            APP_LOAN_MANAGEMENT_PUB_SUB,
            UnderwritingPublishTopic.CUSTOMER_OFFER_APPROVAL.toString(),
            applicationId,
        )
    }

    override fun customerOfferRejected(applicationId: Long) {
        PubSubService.publish(
            APP_LOAN_MANAGEMENT_PUB_SUB,
            UnderwritingPublishTopic.CUSTOMER_OFFER_REJECTED.toString(),
            applicationId,
        )
    }
}