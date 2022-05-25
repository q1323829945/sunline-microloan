package cn.sunline.saas.formula

import java.math.BigDecimal
import java.math.RoundingMode

object CalculateEqualPrincipal {

    fun getPrincipal(amount: BigDecimal,
                     periods: Int): BigDecimal {
        return amount.divide(periods.toBigDecimal(),2, RoundingMode.HALF_UP)
    }
}
