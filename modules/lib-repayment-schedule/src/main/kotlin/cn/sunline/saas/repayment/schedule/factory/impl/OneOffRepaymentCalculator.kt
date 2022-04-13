

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
 * 到期还本还息
 *  到期一次还本付息额=贷款本金×[1+日利率×贷款天数]
 *  日利率 = 年利率 / 360
 */
@Service
class OneOffRepaymentCalculator : BaseRepaymentScheduleCalculator {


    override fun calRepaymentSchedule(dtoRepaymentScheduleCalculateTrial: DTORepaymentScheduleCalculateTrial): DTORepaymentScheduleTrialView {
        val term = dtoRepaymentScheduleCalculateTrial.term
        val paymentMethod = dtoRepaymentScheduleCalculateTrial.paymentMethod
        val amount = dtoRepaymentScheduleCalculateTrial.amount
        val interestRate = dtoRepaymentScheduleCalculateTrial.interestRate
        val repaymentFrequency = dtoRepaymentScheduleCalculateTrial.repaymentFrequency!!
        val repaymentDay = dtoRepaymentScheduleCalculateTrial.repaymentDay
        val startDate = dtoRepaymentScheduleCalculateTrial.startDate
        val endDate = dtoRepaymentScheduleCalculateTrial.endDate
        val baseYearDays = dtoRepaymentScheduleCalculateTrial.baseYearDays
        val repaymentDayType = dtoRepaymentScheduleCalculateTrial.repaymentDayType

        // 日利率
        val loanRateDay = CalcRateComponent.calcBaseRate(interestRate, LoanRateType.DAILY,baseYearDays)

        // 总利息
        var totalInterest = BigDecimal.ZERO

        // 下一个还款日
        val currentRepaymentDateTime = DateTime(startDate)
        var nextRepaymentDateTime = DateTime(endDate)
        val finalRepaymentDateTime = DateTime(endDate)

        // 每期还款详情
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailTrialView> = ArrayList()

        nextRepaymentDateTime = CalcDateComponent.calcNextRepaymentDateTime(currentRepaymentDateTime,finalRepaymentDateTime,nextRepaymentDateTime)

        val interest = CalcInterestComponent.calcDayInterest(amount,loanRateDay,currentRepaymentDateTime,nextRepaymentDateTime)
        totalInterest = totalInterest.add(interest)
        // 计划明细
        dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailTrialView(
            period = 1,
            installment = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(amount,interest),
            principal = amount,
            interest = interest,
            repaymentDate = CalcDateComponent.formatInstantToView(nextRepaymentDateTime.toInstant()),
            remainPrincipal =  BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP)
        )

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
        var totalInterest = BigDecimal.ZERO
        val currentRepaymentDateTime = DateTime(repaymentDate)
        for (detail in repaymentScheduleDetail) {
            val nextRepaymentDateTime = DateTime(detail.repaymentDate)
            // 每期利息
            val interest = CalcInterestComponent.calcDayInterest(remainLoanAmount,loanRateDay,currentRepaymentDateTime,nextRepaymentDateTime)
            detail.interest = interest
            detail.principal = remainLoanAmount
            detail.installment = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(detail.principal,interest)
//            detail.remainPrincipal =  BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
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











