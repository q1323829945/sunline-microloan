package cn.sunline.saas.global.constant

import java.math.BigDecimal

enum class CommissionAmountRangeType(
    val lowerLimit: BigDecimal?,
    val upperLimit: BigDecimal?,
) {
    DEFAULT(null,null),
    LESS_THAN_ONE_MILLION(BigDecimal.ZERO,BigDecimal(1000000)),
    ONE_MILLION_TO_TEN_MILLION(BigDecimal(1000000),BigDecimal(10000000)),
    MORE_THAN_TEN_MILLION(BigDecimal(10000000),null),
}