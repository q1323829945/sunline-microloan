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
    }

    override fun execCreditRisk(partner: String, underwriting: Underwriting) {
        val dtoExecCreditRisk = DTOExecCreditRisk(
            underwriting.id, partner,
            underwriting.customerCreditRate!!
        )
    }

    override fun execRegulatoryCompliance(partner: String, underwriting: Underwriting) {
        val dTOExecRegulatoryCompliance = DTOExecRegulatoryCompliance(
            underwriting.id,partner,
            underwriting.creditRisk!!
        )
    }

    override fun execFraudEvaluation(partner: String, underwriting: Underwriting) {
        val dtoExecFraudEvaluation = DTOExecFraudEvaluation(
            underwriting.id,partner,
            underwriting.fraudEvaluation!!
        )
    }

    override fun updateCustomerOfferStatus(applicationId: Long, operationType: OperationType) {
        val dtoCustomerOffer = DTOCustomerOffer(
            applicationId,
            operationType
        )
    }
}