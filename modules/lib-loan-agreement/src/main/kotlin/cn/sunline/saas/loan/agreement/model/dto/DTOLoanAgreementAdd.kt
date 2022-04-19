package cn.sunline.saas.loan.agreement.model.dto

import cn.sunline.saas.disbursement.arrangement.model.dto.DTODisbursementArrangementAdd
import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementAdd
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.arrangement.model.dto.DTOInterestArrangementAdd
import cn.sunline.saas.repayment.arrangement.model.dto.DTORepaymentArrangementAdd

/**
 * @title: DTOLoanAgreementAdd
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/22 19:07
 */
data class DTOLoanAgreementAdd(
    val productId: Long,
    val term: LoanTermType,
    val amount: String,
    val currency: String,
    val interestArrangement: DTOInterestArrangementAdd,
    val repaymentArrangement: DTORepaymentArrangementAdd,
    val feeArrangement: MutableList<DTOFeeArrangementAdd>,
    val borrower:Long,
    val lender: MutableList<Long>,
    val disbursementArrangement:DTODisbursementArrangementAdd
)
