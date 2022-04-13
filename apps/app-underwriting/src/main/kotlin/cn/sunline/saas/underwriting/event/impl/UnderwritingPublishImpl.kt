package cn.sunline.saas.underwriting.event.impl

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.underwriting.event.UnderwritingPublish
import cn.sunline.saas.underwriting.event.UnderwritingPublishTopic
import cn.sunline.saas.underwriting.event.dto.DTOExecCreditRisk
import cn.sunline.saas.underwriting.event.dto.DTORetrieveCustomerCreditRating
import cn.sunline.saas.underwriting.model.db.Underwriting
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
}