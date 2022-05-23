package cn.sunline.saas.global.constant

import cn.sunline.saas.global.model.TermType

/**
 * @title: RepaymentFrequency
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 16:15
 */
enum class RepaymentFrequency(val term: TermType) {
    ONE_MONTH(TermType(1, TermUnit.MONTH)),
    THREE_MONTHS(TermType(3, TermUnit.MONTH)),
    SIX_MONTHS(TermType(6, TermUnit.MONTH)),
    ONE_YEAR(TermType(12, TermUnit.MONTH)),
}