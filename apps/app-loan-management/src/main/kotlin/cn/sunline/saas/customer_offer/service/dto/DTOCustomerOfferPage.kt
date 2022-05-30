package cn.sunline.saas.customer_offer.service.dto

import cn.sunline.saas.customer.offer.modules.ApplyStatus
import cn.sunline.saas.customer_offer.service.model.UnderwritingType
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.model.CurrencyType

data class DTOCustomerOfferPage(
    val customerOfferId:String,
    val userName: String?,
    val amount:String?,
    val datetime: String,
    val productName:String,
    val status: ApplyStatus,
    val term: LoanTermType?,
    val currency: CurrencyType?,
    val underwritingType: UnderwritingType?,
    val loanAgreementType: AgreementStatus?,
)
