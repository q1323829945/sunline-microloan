package cn.sunline.saas.global.constant

import cn.sunline.saas.global.model.TermType


enum class LoanTermTierType (val fromTerm: TermType, val toTerm: TermType) {
    ONE_MONTH_TO_ONE_MONTH(TermType(1, TermUnit.MONTH), TermType(1, TermUnit.MONTH)),
    ONE_MONTH_TO_THREE_MONTHS(TermType(1, TermUnit.MONTH), TermType(3, TermUnit.MONTH)),
    THREE_MONTHS_TO_SIX_MONTHS(TermType(3, TermUnit.MONTH), TermType(6, TermUnit.MONTH)),
    SIX_MONTHS_TO_ONE_YEAR(TermType(6, TermUnit.MONTH), TermType(3, TermUnit.YEAR)),
    ONE_YEAR_TO_TWO_YEAR(TermType(1, TermUnit.YEAR), TermType(2, TermUnit.YEAR)),
    TWO_YEAR_TO_THREE_YEAR(TermType(2, TermUnit.YEAR), TermType(3, TermUnit.YEAR))
}