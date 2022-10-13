package cn.sunline.saas.interest.component

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.InterestRate
import cn.sunline.saas.interest.model.RatePlanType
import java.math.BigDecimal

/**
 * @title: InterestRateHelper
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/19 10:31
 */
object InterestRateHelper {

    fun getTermOrAmountRate(loanAmount: BigDecimal,loanTerm: LoanTermType, rates: MutableList<InterestRate>?): BigDecimal?{
        val rate = rates?.firstOrNull()
        return if(rate?.fromPeriod!=null){
            rates.sortBy { item -> item.fromPeriod }
            rates.firstOrNull { it.fromPeriod!!.term >= loanTerm.term }?.rate
        }else if (rate?.fromAmountPeriod!=null){
            rates.sortBy { item -> item.fromAmountPeriod }
            rates.firstOrNull { it.fromAmountPeriod!!.amount >= loanAmount }?.rate
        }else{
            null
        }
    }

    fun getTermRate1(loanTerm: LoanTermType, rates: MutableList<InterestRate>?): BigDecimal? {
        rates?.sortBy { item -> item.fromPeriod }
        return rates?.firstOrNull { it.fromPeriod!!.term >= loanTerm.term }?.rate

    }

    fun getAmountRate1(loanAmount: BigDecimal, rates: MutableList<InterestRate>?): BigDecimal? {
        rates?.sortBy { item -> item.fromAmountPeriod }
        return rates?.firstOrNull { it.fromAmountPeriod!!.amount >= loanAmount }?.rate

    }
}