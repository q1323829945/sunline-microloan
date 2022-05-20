package cn.sunline.saas.customer_offer.service.dto

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.model.CurrencyType


data class DTOInvokeCustomerOfferView(
    val applicationId: String,
    val userId: String,
    val customerId: String,
    val productId: String,
    val amount: String,
    val currency: CurrencyType,
    val term: LoanTermType,
    val referenceAccount: DTOInvokeReferenceAccountView?,
    val purpose: String?
)

data class DTOInvokeReferenceAccountView(
    val account: String,
    val accountBank: String
)