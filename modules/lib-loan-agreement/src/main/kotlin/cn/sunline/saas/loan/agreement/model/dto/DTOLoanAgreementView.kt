package cn.sunline.saas.loan.agreement.model.dto

import cn.sunline.saas.fee.arrangement.model.db.FeeArrangement
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementAdd
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.arrangement.model.db.InterestArrangement
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestArrangementAdd
import cn.sunline.saas.loan.agreement.model.db.LoanAgreement
import cn.sunline.saas.repayment.arrangement.model.db.RepaymentArrangement

/**
 * @title: DTOLoanAgreementAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/22 19:07
 */
data class DTOLoanAgreementView(
    val loanAgreement: LoanAgreement,
    val interestArrangement: InterestArrangement,
    val repaymentArrangement: RepaymentArrangement,
    val feeArrangement: MutableList<FeeArrangement>,
)
