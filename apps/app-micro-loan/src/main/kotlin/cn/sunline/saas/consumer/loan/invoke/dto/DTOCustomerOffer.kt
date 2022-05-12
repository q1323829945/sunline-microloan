package cn.sunline.saas.consumer.loan.invoke.dto

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.model.CurrencyType

/**
 * @title: DTOCustomerOffer
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/5 16:53
 */
data class DTOCustomerOffer(
    val applicationId: Long,
    val userId: Long,
    val customerId: Long,
    val productId: Long,
    val amount: String,
    val currency: CurrencyType,
    val term: LoanTermType,
    val referenceAccount: DTOReferenceAccount?,
    val purpose: String?
)

data class DTOReferenceAccount(
    val account: String,
    val accountBank: String
)