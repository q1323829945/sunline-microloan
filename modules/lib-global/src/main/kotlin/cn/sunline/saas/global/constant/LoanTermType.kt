package cn.sunline.saas.global.constant

import cn.sunline.saas.global.model.TermType

/**
 * @title: TermType
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/10 11:10
 */
enum class LoanTermType(val term: TermType) {
    ONE_MONTH(TermType(1, TermUnit.MONTH)),
    THREE_MONTHS(TermType(3, TermUnit.MONTH)),
    SIX_MONTHS(TermType(6, TermUnit.MONTH)),
    ONE_YEAR(TermType(1, TermUnit.YEAR)),
    TWO_YEAR(TermType(2, TermUnit.YEAR)),
    THREE_YEAR(TermType(3, TermUnit.YEAR))
}