package cn.sunline.saas.global.constant

import java.math.BigDecimal

enum class CommissionCountRangeType (
    val lowerLimit: BigDecimal?,
    val upperLimit: BigDecimal?,
) {
    DEFAULT(null,null),
    LESS_THAN_ONE_THOUSAND(BigDecimal.ZERO,BigDecimal(1000)),
    ONE_THOUSAND_TO_TEN_THOUSAND(BigDecimal(1000),BigDecimal(10000)),
    MORE_THAN_TEN_THOUSAND(BigDecimal(10000),null)
}