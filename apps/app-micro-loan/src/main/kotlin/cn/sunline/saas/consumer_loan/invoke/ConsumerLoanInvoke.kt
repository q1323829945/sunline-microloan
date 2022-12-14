package cn.sunline.saas.consumer_loan.invoke

import cn.sunline.saas.consumer_loan.invoke.dto.DTOCustomerOffer
import cn.sunline.saas.consumer_loan.invoke.dto.DTOLoanProduct
import cn.sunline.saas.interest.model.InterestRate

/**
 * @title: ConsumerLoanInvoke
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/5 15:32
 */
interface ConsumerLoanInvoke {

    fun retrieveCustomerOffer(applicationId: Long): DTOCustomerOffer

    fun retrieveLoanProduct(productId: Long): DTOLoanProduct

    fun retrieveBaseInterestRate(ratePlanId:Long):MutableList<InterestRate>?

}
