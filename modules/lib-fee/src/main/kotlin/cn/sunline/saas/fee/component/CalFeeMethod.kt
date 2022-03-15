package cn.sunline.saas.fee.component

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * @title: CalFeeMethod
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/14 10:31
 */
object CalFeeMethod {
    private val mc = MathContext(2, RoundingMode.HALF_UP)

    fun calFeeAmount(baseAmount:BigDecimal,feeRatio:BigDecimal):BigDecimal{
        return baseAmount.multiply(feeRatio).divide(BigDecimal(100),mc)
    }
}