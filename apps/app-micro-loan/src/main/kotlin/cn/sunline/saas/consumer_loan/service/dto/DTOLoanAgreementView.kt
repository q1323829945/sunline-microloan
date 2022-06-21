package cn.sunline.saas.consumer_loan.service.dto

import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.model.CurrencyType
import java.math.BigDecimal

data class DTOLoanAgreementView(
    val id: String,
    val status: AgreementStatus
)

data class DTOLoanAgreementViewInfo(
    val id: Long,
    val amount: BigDecimal,
    val currency: CurrencyType,
    val productId: Long,
    val purpose: String?,
    val applicationId: Long,
    val userId: Long
)