package cn.sunline.saas.underwriting.event.impl

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.underwriting.event.DTORetrieveCustomerCreditRating
import cn.sunline.saas.underwriting.event.UnderwritingPublish
import cn.sunline.saas.underwriting.event.UnderwritingPublishTopic
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

    override fun retrieveCustomerCreditRating(partner: String, customerId: Long) {
        val dtoRetrieveCustomerCreditRating = DTORetrieveCustomerCreditRating(partner, customerId)
        DaprHelper.publish(
            PUBSUB_NAME,
            UnderwritingPublishTopic.RETRIEVE_CUSTOMER_CREDIT_RATING.toString(),
            dtoRetrieveCustomerCreditRating
        )
    }
}