package cn.sunline.saas.global.constant

import cn.sunline.saas.global.model.TermType
import java.math.BigDecimal
import java.math.RoundingMode


enum class LoanAmountTierType(val amount: BigDecimal) {
    ZERO(BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)),
    ONE_HUNDRED_THOUSAND(BigDecimal(100000).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)),
    THREE_HUNDRED_THOUSAND(BigDecimal(300000).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)),
    SIX_HUNDRED_THOUSAND(BigDecimal(600000).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)),
    ONE_MILLION(BigDecimal(1000000).setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP))
}