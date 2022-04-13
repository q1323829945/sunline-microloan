
















package cn.sunline.saas.repayment.schedule.factory.impl

import cn.sunline.saas.repayment.schedule.component.*
import cn.sunline.saas.repayment.schedule.factory.BaseRepaymentScheduleCalculator
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculateTrial
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleDetailTrialView
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleTrialView
import cn.sunline.saas.repayment.schedule.model.enum.LoanRateType
import org.joda.time.DateTime
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

/**
 * 按期付息到期还款
 *      每月还利息，到期还本金。每月的还款金额X,融资金额A,年化收益B,总期数M,,每月天数D
 *      每月还款本金：0，最后一个月为A
 *      每月还款利息：A*B/360*本期天数
 *      X=每月还款本金+每月还款利息101
 */
@Service
class PayInterestSchedulePrincipalMaturityCalculator : BaseRepaymentScheduleCalculator {


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

        // 应还期数
        val periods = CalcPeriodComponent.calcDuePeriods(startDate,endDate,repaymentDay,repaymentFrequency)

        // 日利率
        val loanRateDay = CalcRateComponent.calcBaseRate(interestRate, LoanRateType.DAILY,baseYearDays)

        // 每期利息
        var interest: BigDecimal

        // 总利息
        var totalInterest = BigDecimal.ZERO

        // 下一个还款日
        val maximumValue = startDate.toDateTime().dayOfMonth().maximumValue
        if(repaymentDay>maximumValue){
            repaymentDay = maximumValue
        }
        var currentRepaymentDateTime = DateTime(startDate)
        var nextRepaymentDateTime = DateTime(currentRepaymentDateTime.year, currentRepaymentDateTime.monthOfYear, repaymentDay,0,0).plusMonths(repaymentFrequency.months)
        val finalRepaymentDateTime = DateTime(endDate)


        // 每期还款详情
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailTrialView> = ArrayList()
        for (i in 0 until periods) {

            nextRepaymentDateTime = CalcDateComponent.calcNextRepaymentDateTime(currentRepaymentDateTime, finalRepaymentDateTime, nextRepaymentDateTime)
            interest = CalcInterestComponent.calcDayInterest(amount,loanRateDay,currentRepaymentDateTime,nextRepaymentDateTime)
            totalInterest = totalInterest.add(interest)
            // 计划明细
            dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailTrialView(
                period = i + 1,
                installment = if (periods == i + 1) CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(amount,interest) else interest.setScale(2,RoundingMode.HALF_UP),
                principal = if(periods == i + 1) amount else BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP),
                interest = interest.setScale(2,RoundingMode.HALF_UP),
                repaymentDate = CalcDateComponent.formatInstantToView(nextRepaymentDateTime.toInstant()),
                remainPrincipal =  if(periods == i + 1) BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP) else amount
            )

            currentRepaymentDateTime = nextRepaymentDateTime
            nextRepaymentDateTime = nextRepaymentDateTime.plusMonths(1 * repaymentFrequency.months)
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

        // 日利率
        val loanRateDay = CalcRateComponent.calcBaseRate(interestRate, LoanRateType.DAILY,baseYearDays)
        // 剩余期数和当前期数
        val result = CalcPeriodComponent.calcRemainPeriods(repaymentDate,repaymentScheduleDetail)
        val currentPeriod = result[0]
        val finalPeriod = result[1]
        var currentRepaymentDateTime = DateTime(repaymentDate)
        var totalInterest = BigDecimal.ZERO
        for (detail in repaymentScheduleDetail) {
            if (currentPeriod <= detail.period) {
                val nextRepaymentDateTime = DateTime(detail.repaymentDate)
                // 每期利息
                val interest = CalcInterestComponent.calcDayInterest(remainLoanAmount,loanRateDay,currentRepaymentDateTime,nextRepaymentDateTime)
                detail.interest = interest
                detail.principal = if(finalPeriod == detail.period) remainLoanAmount else BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP)
                detail.installment = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(detail.principal,interest)
//                detail.remainPrincipal = if(finalPeriod == detail.period) BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP) else remainLoanAmount
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


























