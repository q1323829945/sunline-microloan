package cn.sunline.saas.customer.credit.rating.wrapper.service

import cn.sunline.saas.customer.credit.rating.wrapper.dto.DTOCallBackCustomerCreditRating
import cn.sunline.saas.customer.credit.rating.wrapper.dto.DTOCreditRating
import cn.sunline.saas.dapr_wrapper.pubsub.PubSubService
import org.springframework.stereotype.Service

@Service
class CustomerCreditRatingService {


    fun getCreditRating(creditRating: DTOCreditRating){
        //TODO:
        val customerCreditRate = "10"

        PubSubService.publish(
            "app-loan-management",
            "CALL_BACK_CUSTOMER_CREDIT_RATING",
            DTOCallBackCustomerCreditRating(creditRating.applicationId,customerCreditRate)
        )
    }
}