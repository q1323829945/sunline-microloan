

package cn.sunline.saas.repayment.schedule.component

import cn.sunline.saas.interest.constant.BaseYearDays
import cn.sunline.saas.interest.util.InterestRateUtil
import cn.sunline.saas.repayment.schedule.model.enum.LoanRateType
import java.math.BigDecimal
import java.math.RoundingMode

object CalcRateComponent {

    fun calcBaseRate(loanRate: BigDecimal,loanRateType : LoanRateType, baseYearDays: BaseYearDays?): BigDecimal {
        return when (loanRateType) {
            LoanRateType.DAILY -> InterestRateUtil.toDayRate(baseYearDays!!,loanRate).setScale(6,RoundingMode.HALF_UP)
            LoanRateType.MONTHLY -> InterestRateUtil.toMonthRate(loanRate).setScale(6,RoundingMode.HALF_UP)
        }
    }
}