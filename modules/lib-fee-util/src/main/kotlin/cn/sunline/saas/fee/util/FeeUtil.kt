package cn.sunline.saas.fee.util

import cn.sunline.saas.fee.exception.FeeConfigException
import cn.sunline.saas.fee.constant.FeeMethodType
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

/**
 * @title: FeeUtil
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/25 8:49
 */
object FeeUtil {
    private val mc = MathContext(2, RoundingMode.HALF_UP)

    fun calFeeAmount(baseAmount: BigDecimal, feeRatio: BigDecimal): BigDecimal {
        return baseAmount.multiply(feeRatio).divide(BigDecimal(100), mc)
    }

    fun validFeeConfig(feeMethodType: FeeMethodType, feeAmount: BigDecimal?, feeRate: BigDecimal?) {
        if (feeMethodType == FeeMethodType.FEE_RATIO && feeRate == null) {
            throw FeeConfigException("Fee calculation method config error")
        }
        if (feeMethodType == FeeMethodType.FIX_AMOUNT && feeAmount == null) {
            throw FeeConfigException("Fee calculation method config error")
        }
    }
}