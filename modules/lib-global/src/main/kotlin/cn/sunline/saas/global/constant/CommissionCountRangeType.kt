package cn.sunline.saas.global.constant

import java.math.BigDecimal

enum class CommissionCountRangeType (
    val lowerLimit: Long?,
    val upperLimit: Long?,
) {
    DEFAULT(null,null),
    LESS_THAN_ONE_THOUSAND(0,1000),
    ONE_THOUSAND_TO_TEN_THOUSAND(1000,10000),
    MORE_THAN_TEN_THOUSAND(10000,null)
}