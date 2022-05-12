package cn.sunline.saas.underwriting.event.impl

import cn.sunline.saas.underwriting.event.UnderwritingPublish
import cn.sunline.saas.underwriting.event.UnderwritingPublishTopic
import cn.sunline.saas.underwriting.db.Underwriting
import cn.sunline.saas.underwriting.event.dto.*
import cn.sunline.saas.underwriting.db.OperationType
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
        DaprHelper.publish(
            PUBSUB_NAME,
            UnderwritingPublishTopic.RETRIEVE_CUSTOMER_CREDIT_RATING.toString(),
            dtoRetrieveCustomerCreditRating
        )
    }

    override fun execCreditRisk(partner: String, underwriting: Underwriting) {
        val dtoExecCreditRisk = DTOExecCreditRisk(
            underwriting.id, partner,
            underwriting.customerCreditRate!!
        )
        DaprHelper.publish(
            PUBSUB_NAME,
            UnderwritingPublishTopic.EXECUTE_CREDIT_RISK.toString(),
            dtoExecCreditRisk
        )
    }

    override fun execRegulatoryCompliance(partner: String, underwriting: Underwriting) {
        val dTOExecRegulatoryCompliance = DTOExecRegulatoryCompliance(
            underwriting.id,partner,
            underwriting.creditRisk!!
        )

        DaprHelper.publish(
            PUBSUB_NAME,
            UnderwritingPublishTopic.EXECUTE_REGULATORY_COMPLIANCE.toString(),
            dTOExecRegulatoryCompliance
        )
    }

    override fun execFraudEvaluation(partner: String, underwriting: Underwriting) {
        val dtoExecFraudEvaluation = DTOExecFraudEvaluation(
            underwriting.id,partner,
            underwriting.fraudEvaluation!!
        )

        DaprHelper.publish(
            PUBSUB_NAME,
            UnderwritingPublishTopic.FRAUD_EVALUATION.toString(),
            dtoExecFraudEvaluation
        )
    }

    override fun updateCustomerOfferStatus(applicationId: Long, operationType: OperationType) {
        val dtoCustomerOffer = DTOCustomerOffer(
            applicationId,
            operationType
        )
        DaprHelper.publish(
            PUBSUB_NAME,
            UnderwritingPublishTopic.CUSTOMER_OFFER_STATUS.toString(),
            dtoCustomerOffer
        )
    }
}