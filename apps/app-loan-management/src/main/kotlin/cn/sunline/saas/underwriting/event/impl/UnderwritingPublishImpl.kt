package cn.sunline.saas.underwriting.event.impl

import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import cn.sunline.saas.underwriting.event.UnderwritingPublish
import cn.sunline.saas.underwriting.event.UnderwritingPublishTopic
import cn.sunline.saas.underwriting.db.Underwriting
import cn.sunline.saas.underwriting.event.dto.*
import cn.sunline.saas.underwriting.db.UnderwritingType
import org.springframework.stereotype.Component

/**
 * @title: UnderwritingPublish
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/7 15:41
 */
@Component
class UnderwritingPublishImpl : UnderwritingPublish {

    private val PUBSUB_NAME = "underwriting-pub-sub"

    override fun retrieveCustomerCreditRating(applicationId: Long, partner: String, customerId: Long) {
        val dtoRetrieveCustomerCreditRating = DTORetrieveCustomerCreditRating(applicationId, partner, customerId)
        PubSubService.publish(
            PUBSUB_NAME,
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
            PUBSUB_NAME,
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
            PUBSUB_NAME,
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
            PUBSUB_NAME,
            UnderwritingPublishTopic.FRAUD_EVALUATION.toString(),
            dtoExecFraudEvaluation,
        )
    }

    override fun updateCustomerOfferStatus(applicationId: Long, underwritingType: UnderwritingType) {
        val dtoCustomerOffer = DTOCustomerOffer(
            applicationId,
            underwritingType
        )

        PubSubService.publish(
            PUBSUB_NAME,
            UnderwritingPublishTopic.CUSTOMER_OFFER_STATUS.toString(),
            dtoCustomerOffer,
        )
    }


    override fun initiateLoanAgreement(applicationId: String) {
        PubSubService.publish(
            PUBSUB_NAME,
            UnderwritingPublishTopic.INITIATE_LOAN_AGREEMENT.toString(),
            applicationId
        )
    }
}