package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.AgreementType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.loan.agreement.model.LoanAgreementInvolvementType
import java.math.BigDecimal

data class DTOLoanAgreementView (
    val id:String,
    val agreementType: AgreementType,
    val signedDate:String,
    val fromDateTime:String,
    val toDateTime:String,
    val term: LoanTermType,
    val version: Int,
    val status: AgreementStatus,
    val amount: String,
    val currency: CurrencyType,
    val productId: String,
    val agreementDocument: String?,
    val purpose: String?,
    val applicationId: String,
    val userId: String,
    val involvements: List<LoanAgreementInvolvementView>,
)

data class LoanAgreementInvolvementView(
    val id:String,
    val partyId: String,
    val involvementType: LoanAgreementInvolvementType,
)