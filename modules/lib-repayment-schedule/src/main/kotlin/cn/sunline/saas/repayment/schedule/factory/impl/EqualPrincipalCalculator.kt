














package cn.sunline.saas.repayment.schedule.factory.impl

import cn.sunline.saas.interest.util.InterestRateUtil
import cn.sunline.saas.interest.util.InterestUtil
import cn.sunline.saas.repayment.schedule.component.*
import cn.sunline.saas.repayment.schedule.factory.BaseRepaymentScheduleCalculator
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.*
import org.joda.time.DateTime
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.collections.ArrayList


/**
 * 等额本金 每月还款额=贷款本金÷贷款期期数+（本金-已归还本金累计额）×月利率
 *        每季还款额=贷款本金÷贷款期季数+（本金-已归还本金累计额）×季利率
 */
@Service
class EqualPrincipalCalculator: BaseRepaymentScheduleCalculator {

    override fun calRepaymentSchedule(dtoRepaymentScheduleCalculateTrial: DTORepaymentScheduleCalculateTrial): DTORepaymentScheduleTrialView {

        val paymentMethod = dtoRepaymentScheduleCalculateTrial.paymentMethod
        val amount = dtoRepaymentScheduleCalculateTrial.amount
        val interestRate = dtoRepaymentScheduleCalculateTrial.interestRate
        val repaymentFrequency = dtoRepaymentScheduleCalculateTrial.repaymentFrequency
        var repaymentDay = dtoRepaymentScheduleCalculateTrial.repaymentDay
        val startDate = dtoRepaymentScheduleCalculateTrial.startDate
        val endDate = dtoRepaymentScheduleCalculateTrial.endDate
        val baseYearDays = dtoRepaymentScheduleCalculateTrial.baseYearDays

        // 开始日期
        var currentRepaymentDateTime = startDate.toDateTime()

        // 下一个还款日
        val maximumValue = startDate.toDateTime().dayOfMonth().maximumValue
        if(repaymentDay>maximumValue){
            repaymentDay = maximumValue
        }
        var nextRepaymentDateTime = DateTime(currentRepaymentDateTime.year, currentRepaymentDateTime.monthOfYear,repaymentDay,0,0).plusMonths(repaymentFrequency.months)

        // 结束日期
        val finalRepaymentDateTime = endDate.toDateTime()

        // 应还期数
        val periods = CalcPeriodComponent.calcDuePeriods(startDate, endDate, repaymentDay, repaymentFrequency)

        // 每期应还本金
        var principal = CalcInstallmentComponent.calcBasePrincipal(DTOPrincipalCalculator(amount,paymentMethod,BigDecimal.ZERO,periods,0,repaymentFrequency))

        // 每期利率
        val loanRateMonth = InterestRateUtil.toMonthRate(interestRate)

        // 日利率
        val loanRateDay = InterestRateUtil.toDayRate(baseYearDays,interestRate)

        // 每期利息
        var interest : BigDecimal

        // 剩余本金
        var remainPrincipal: BigDecimal = amount

        // 每期还款详情
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailTrialView> = ArrayList()
        for (i in 0 until periods) {
            // 还款日
            nextRepaymentDateTime = CalcDateComponent.calcNextRepaymentDateTime(currentRepaymentDateTime,finalRepaymentDateTime,nextRepaymentDateTime)

            // 首期或未期不足月先按日计算利息
            interest = if(nextRepaymentDateTime.compareTo(currentRepaymentDateTime.plusMonths(1 * repaymentFrequency.months)) != 0){
                InterestUtil.calDaysInterest(remainPrincipal,loanRateDay,currentRepaymentDateTime.toInstant(),nextRepaymentDateTime.toInstant())
            }else{
                // 每期利息
                InterestUtil.calMonthInterest(remainPrincipal,loanRateMonth.multiply(repaymentFrequency.months.toBigDecimal()))
            }

            if (i + 1 == periods && (remainPrincipal != BigDecimal.ZERO)) {
                principal = remainPrincipal
            }
            remainPrincipal = remainPrincipal.subtract(principal)

            // 计划明细
            dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailTrialView(
                period = i + 1,
                installment = CalcInstallmentComponent.calcBaseRepaymentInstallment(principal,interest),
                principal = principal,
                interest = interest,
                repaymentDate = CalcDateComponent.formatInstantToView(nextRepaymentDateTime.toInstant()),
                remainPrincipal = remainPrincipal
            )
            currentRepaymentDateTime = nextRepaymentDateTime
            nextRepaymentDateTime = nextRepaymentDateTime.plusMonths(1 * repaymentFrequency.months)
        }
        // 还款计划概述
        return DTORepaymentScheduleTrialView(
            interestRate = interestRate,
            schedule = dtoRepaymentScheduleDetailTrialView
        )
    }

    override fun calResetRepaymentSchedule(dtoRepaymentScheduleResetCalculate: DTORepaymentScheduleResetCalculate): RepaymentSchedule {

        var remainLoanAmount = dtoRepaymentScheduleResetCalculate.remainLoanAmount
        val repaymentDate = dtoRepaymentScheduleResetCalculate.repaymentDate
        val baseYearDays = dtoRepaymentScheduleResetCalculate.baseYearDays
        val repaymentSchedule = dtoRepaymentScheduleResetCalculate.oldRepaymentSchedule
        val interestRate = dtoRepaymentScheduleResetCalculate.oldRepaymentSchedule.interestRate
        val paymentMethod = dtoRepaymentScheduleResetCalculate.paymentMethod
        val repaymentScheduleDetail = repaymentSchedule.schedule
        val repaymentFrequency = dtoRepaymentScheduleResetCalculate.repaymentFrequency

        // 每期还款详情
        val repaymentScheduleDetails: MutableList<RepaymentScheduleDetail> = ArrayList()

        // 每期利率
        val loanRateMonth = InterestRateUtil.toMonthRate(interestRate)

        // 日利率
        val loanRateDay = InterestRateUtil.toDayRate(baseYearDays,interestRate)

        // 剩余期数和当前期数
        val result = CalcPeriodComponent.calcRemainPeriods(repaymentDate, repaymentScheduleDetail)
        val currentPeriod = result[0]
        val finalPeriod = result[1]
        val periods = result[2]

        // 每期应还本金
        var principal = CalcInstallmentComponent.calcBasePrincipal(DTOPrincipalCalculator(remainLoanAmount, paymentMethod, BigDecimal.ZERO, periods, 0, null))
        var currentRepaymentDateTime = repaymentDate.toDateTime()
        for (detail in repaymentScheduleDetail) {
            if (currentPeriod <= detail.period) {
                val nextRepaymentDateTime = DateTime(detail.repaymentDate)
                // 首期或未期不足月先按日计算利息
                val interest = if(nextRepaymentDateTime.compareTo(currentRepaymentDateTime.plusMonths(1 * repaymentFrequency.months)) != 0){
                    InterestUtil.calDaysInterest(remainLoanAmount,loanRateDay,currentRepaymentDateTime.toInstant(),nextRepaymentDateTime.toInstant())
                }else{
                    // 每期利息
                    InterestUtil.calMonthInterest(remainLoanAmount,loanRateMonth.multiply(repaymentFrequency.months.toBigDecimal()))
                }

                if (finalPeriod == detail.period && (remainLoanAmount != BigDecimal.ZERO)) {
                    principal = remainLoanAmount
                }
                remainLoanAmount = remainLoanAmount.subtract(principal)

                detail.principal = principal
                detail.interest = interest
                detail.installment = CalcInstallmentComponent.calcBaseRepaymentInstallment(principal, interest)

                // 日期滚动
                currentRepaymentDateTime = nextRepaymentDateTime
            }
            repaymentScheduleDetails += detail
        }
        repaymentSchedule.schedule = repaymentScheduleDetails
        repaymentSchedule.interestRate = interestRate
        return repaymentSchedule
    }
}