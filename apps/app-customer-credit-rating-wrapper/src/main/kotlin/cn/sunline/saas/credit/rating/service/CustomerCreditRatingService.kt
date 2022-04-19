package cn.sunline.saas.credit.rating.service

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.credit.rating.dto.DTOCallBackCustomerCreditRating
import cn.sunline.saas.credit.rating.dto.DTOCreditRating
import org.springframework.stereotype.Service

@Service
class CustomerCreditRatingService {


    fun getCreditRating(creditRating: DTOCreditRating){
        //TODO:
        val customerCreditRate = "10"

        DaprHelper.publish(
            "underwriting-pub-sub",
            "CALL_BACK_CUSTOMER_CREDIT_RATING",
            DTOCallBackCustomerCreditRating(creditRating.data.applicationId,customerCreditRate)
        )
    }
}