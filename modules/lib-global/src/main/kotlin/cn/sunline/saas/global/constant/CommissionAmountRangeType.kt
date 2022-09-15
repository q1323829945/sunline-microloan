package cn.sunline.saas.global.constant

import java.math.BigDecimal

enum class CommissionAmountRangeType(
    val lowerLimit: Long?,
    val upperLimit: Long?,
) {
    DEFAULT(null,null),
    LESS_THAN_ONE_MILLION(0,1000000),
    ONE_MILLION_TO_TEN_MILLION(1000000,10000000),
    MORE_THAN_TEN_MILLION(10000000,null),
}