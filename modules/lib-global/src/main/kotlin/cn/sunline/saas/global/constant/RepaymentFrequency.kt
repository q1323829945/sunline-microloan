package cn.sunline.saas.global.constant

/**
 * @title: RepaymentFrequency
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 16:15
 */
enum class RepaymentFrequency(var months: Int) {
    ONE_MONTH(1),
    THREE_MONTHS(3),
    SIX_MONTHS(6) ,
    TWELVE_MONTHS (12)
}