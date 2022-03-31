

package cn.sunline.saas.repayment.schedule.component

import cn.sunline.saas.interest.component.CalInterestRateComponent
import cn.sunline.saas.interest.model.BaseYearDays
import cn.sunline.saas.repayment.schedule.model.enum.LoanRateType
import java.math.BigDecimal
import java.math.RoundingMode

object CalcRateComponent {

    fun calcBaseRate(loanRate: BigDecimal,loanRateType : LoanRateType, baseYearDays: BaseYearDays?): BigDecimal {
        return when (loanRateType) {
            LoanRateType.DAILY -> CalInterestRateComponent.toDayRate(baseYearDays!!,loanRate).setScale(6,RoundingMode.HALF_UP)
            LoanRateType.MONTHLY -> CalInterestRateComponent.toMonthRate(loanRate).setScale(6,RoundingMode.HALF_UP)
        }
    }
}