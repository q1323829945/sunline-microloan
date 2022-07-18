package cn.sunline.saas.formula

import cn.sunline.saas.global.constant.LoanFeeType

object CalculateFee {

    fun calcFee(loanFeeType: LoanFeeType) {
        when (loanFeeType) {
            LoanFeeType.DISBURSEMENT -> {

            }
            LoanFeeType.PREPAYMENT -> {}
            LoanFeeType.OVERDUE -> {}
        }
    }



}