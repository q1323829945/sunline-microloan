package cn.sunline.saas.consumer.loan.invoke

import cn.sunline.saas.consumer.loan.invoke.dto.DTOCustomerOffer
import cn.sunline.saas.consumer.loan.invoke.dto.DTOLoanProduct

/**
 * @title: ConsumerLoanInvoke
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/5 15:32
 */
interface ConsumerLoanInvoke {

    fun retrieveCustomerOffer(applicationId: Long): DTOCustomerOffer

    fun retrieveLoanProduct(productId: Long): DTOLoanProduct
}