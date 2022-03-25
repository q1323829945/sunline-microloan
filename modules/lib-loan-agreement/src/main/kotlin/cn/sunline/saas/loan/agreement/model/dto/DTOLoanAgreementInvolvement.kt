package cn.sunline.saas.loan.agreement.model.dto

import cn.sunline.saas.loan.agreement.model.LoanAgreementInvolvementType

/**
 * @title: DTOLoanAgreementInvolvement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/25 16:44
 */
data class DTOLoanAgreementInvolvement(
    val partyId: Long,
    val involvementType: LoanAgreementInvolvementType
)
