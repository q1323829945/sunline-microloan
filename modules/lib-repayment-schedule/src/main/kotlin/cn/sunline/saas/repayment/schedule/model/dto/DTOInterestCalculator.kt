package cn.sunline.saas.repayment.schedule.model.dto

import cn.sunline.saas.global.constant.RepaymentFrequency
import org.joda.time.DateTime
import java.math.BigDecimal


data class DTOInterestCalculator(
    var calcAmount: BigDecimal,
    var loanRateMonth: BigDecimal,
    var loanRateDay: BigDecimal,
    var currentRepaymentDateTime: DateTime,
    var nextRepaymentDateTime: DateTime,
    var repaymentFrequency: RepaymentFrequency? = null,
)