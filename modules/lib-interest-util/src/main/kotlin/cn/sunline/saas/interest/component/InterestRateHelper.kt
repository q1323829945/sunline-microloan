package cn.sunline.saas.interest.component

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.InterestRate
import java.math.BigDecimal

/**
 * @title: InterestRateHelper
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/19 10:31
 */
object InterestRateHelper {

    fun getRate(loanTerm: LoanTermType, rates: MutableList<InterestRate>?): BigDecimal? {
        rates?.sortBy { item -> item.period.term }
        return rates?.firstOrNull() { it.period.term >= loanTerm.term }?.rate
    }
}