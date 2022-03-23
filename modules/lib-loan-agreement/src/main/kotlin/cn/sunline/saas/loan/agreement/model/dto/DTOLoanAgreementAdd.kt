package cn.sunline.saas.loan.agreement.model.dto

import cn.sunline.saas.global.constant.AgreementType
import cn.sunline.saas.global.constant.LoanTermType

/**
 * @title: DTOLoanAgreementAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/22 19:07
 */
data class DTOLoanAgreementAdd(
    val term: LoanTermType,
    val amount: String,
    val currency: String
)
