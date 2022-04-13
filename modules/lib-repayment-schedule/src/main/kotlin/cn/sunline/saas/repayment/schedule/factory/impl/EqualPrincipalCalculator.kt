














package cn.sunline.saas.repayment.schedule.factory.impl

import cn.sunline.saas.repayment.schedule.component.*
import cn.sunline.saas.repayment.schedule.factory.BaseRepaymentScheduleCalculator
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.*
import cn.sunline.saas.repayment.schedule.model.enum.LoanRateType
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

        val term = dtoRepaymentScheduleCalculateTrial.term
        val paymentMethod = dtoRepaymentScheduleCalculateTrial.paymentMethod
        val amount = dtoRepaymentScheduleCalculateTrial.amount
        val interestRate = dtoRepaymentScheduleCalculateTrial.interestRate
        val repaymentFrequency = dtoRepaymentScheduleCalculateTrial.repaymentFrequency!!
        var repaymentDay = dtoRepaymentScheduleCalculateTrial.repaymentDay
        val startDate = dtoRepaymentScheduleCalculateTrial.startDate
        val endDate = dtoRepaymentScheduleCalculateTrial.endDate
        val baseYearDays = dtoRepaymentScheduleCalculateTrial.baseYearDays
        val repaymentDayType = dtoRepaymentScheduleCalculateTrial.repaymentDayType


        // 开始日期
        var currentRepaymentDateTime = DateTime(startDate)

        // 下一个还款日
        val maximumValue = startDate.toDateTime().dayOfMonth().maximumValue
        if(repaymentDay>maximumValue){
            repaymentDay = maximumValue
        }
        var nextRepaymentDateTime = DateTime(currentRepaymentDateTime.year, currentRepaymentDateTime.monthOfYear,repaymentDay,0,0).plusMonths(repaymentFrequency.months)

        // 结束日期
        val finalRepaymentDateTime = DateTime(endDate)

        // 应还期数
        val periods = CalcPeriodComponent.calcDuePeriods(startDate, endDate, repaymentDay, repaymentFrequency)

        // 每期应还本金
        var principal = CalcPrincipalComponent.calcBasePrincipal(
            DTOPrincipalCalculator(amount,paymentMethod,BigDecimal.ZERO,periods,0,repaymentFrequency)
        )

        // 月利率
        val loanRateMonth = CalcRateComponent.calcBaseRate(interestRate, LoanRateType.MONTHLY, null)

        // 日利率
        val loanRateDay = CalcRateComponent.calcBaseRate(interestRate, LoanRateType.DAILY, baseYearDays)

        // 总利息
        var totalInterest = BigDecimal.ZERO

        // 剩余本金
        var remainPrincipal: BigDecimal = amount

        // 每期还款详情
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailTrialView> = ArrayList()

        for (i in 0 until periods) {
            // 还款日
            nextRepaymentDateTime = CalcDateComponent.calcNextRepaymentDateTime(currentRepaymentDateTime,finalRepaymentDateTime,nextRepaymentDateTime)

            // 每期利息
            val interest = CalcInterestComponent.calcBaseInterest(
                DTOInterestCalculator(remainPrincipal,loanRateMonth,loanRateDay,currentRepaymentDateTime,nextRepaymentDateTime,repaymentFrequency)
            )
            totalInterest = totalInterest.add(interest)

            if (i + 1 == periods && (remainPrincipal != BigDecimal.ZERO)) {
                principal = remainPrincipal
            }
            remainPrincipal = remainPrincipal.subtract(principal)

            // 计划明细
            dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailTrialView(
                period = i + 1,
                installment = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(principal,interest),
                principal = principal,
                interest = interest,
                repaymentDate = CalcDateComponent.formatInstantToView(nextRepaymentDateTime.toInstant()),
                remainPrincipal = remainPrincipal
            )

            currentRepaymentDateTime = nextRepaymentDateTime
            nextRepaymentDateTime = nextRepaymentDateTime.plusMonths(1 * repaymentFrequency.ordinal)
        }

        // 还款计划概述
        val totalRepayment = amount.add(totalInterest)
        return DTORepaymentScheduleTrialView(
            interestRate = interestRate.setScale(6, RoundingMode.HALF_UP),
            schedule = dtoRepaymentScheduleDetailTrialView
        )
    }

    override fun calResetRepaymentSchedule(
        dtoRepaymentScheduleResetCalculate: DTORepaymentScheduleResetCalculate
    ): RepaymentSchedule {

        var remainLoanAmount = dtoRepaymentScheduleResetCalculate.remainLoanAmount
        val repaymentDate = dtoRepaymentScheduleResetCalculate.repaymentDate
        val baseYearDays = dtoRepaymentScheduleResetCalculate.baseYearDays
        val repaymentSchedule = dtoRepaymentScheduleResetCalculate.oldRepaymentSchedule
        val interestRate = dtoRepaymentScheduleResetCalculate.oldRepaymentSchedule.interestRate
        val repaymentFrequency = dtoRepaymentScheduleResetCalculate.repaymentFrequency
        val paymentMethod = dtoRepaymentScheduleResetCalculate.paymentMethod
        val repaymentScheduleDetail = repaymentSchedule.schedule


        // 每期还款详情
        val repaymentScheduleDetails: MutableList<RepaymentScheduleDetail> = ArrayList()

        // 月利率
        val loanRateMonth = CalcRateComponent.calcBaseRate(interestRate, LoanRateType.MONTHLY, null)

        // 日利率
        val loanRateDay = CalcRateComponent.calcBaseRate(interestRate, LoanRateType.DAILY, baseYearDays)

        // 剩余期数和当前期数
        val result = CalcPeriodComponent.calcRemainPeriods(repaymentDate, repaymentScheduleDetail)
        val currentPeriod = result[0]
        val finalPeriod = result[1]
        val periods = result[2]

        // 每期应还本金
        var principal = CalcPrincipalComponent.calcBasePrincipal(
            DTOPrincipalCalculator(
                remainLoanAmount, paymentMethod, BigDecimal.ZERO, periods, 0, null
            )
        )
        var currentRepaymentDateTime = DateTime(repaymentDate)
        var totalInterest = BigDecimal.ZERO
        for (detail in repaymentScheduleDetail) {
            if (currentPeriod <= detail.period) {
                val nextRepaymentDateTime = DateTime(detail.repaymentDate)
                // 每期利息
                val interest = CalcInterestComponent.calcBaseInterest(
                    DTOInterestCalculator(
                        remainLoanAmount,loanRateMonth,loanRateDay,currentRepaymentDateTime,nextRepaymentDateTime,null
                    )
                )
                if (finalPeriod == detail.period && (remainLoanAmount != BigDecimal.ZERO)) {
                    principal = remainLoanAmount
                }
                remainLoanAmount = remainLoanAmount.subtract(principal)

                detail.principal = principal
                detail.interest = interest
//                detail.remainPrincipal = remainLoanAmount
                detail.installment = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(principal, interest)

                // 日期滚动
                currentRepaymentDateTime = nextRepaymentDateTime
            }

            totalInterest = totalInterest.add(detail.interest)
            repaymentScheduleDetails += detail
        }
        repaymentSchedule.schedule = repaymentScheduleDetails
        repaymentSchedule.interestRate = interestRate.setScale(6, RoundingMode.HALF_UP)
//        repaymentSchedule.totalInterest = totalInterest
//        repaymentSchedule.totalRepayment = repaymentSchedule.amount.add(totalInterest)


        return repaymentSchedule
    }
}