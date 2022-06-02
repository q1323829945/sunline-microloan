package cn.sunline.saas.repayment.schedule.factory.impl

import cn.sunline.saas.formula.CalculateInterest
import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.component.CalcInstallmentComponent
import cn.sunline.saas.repayment.schedule.component.CalcPeriodComponent
import cn.sunline.saas.repayment.schedule.factory.BaseRepaymentScheduleService
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculateTrial
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleDetailView
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleView
import org.joda.time.DateTime
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 按期付息到期还款
 *      每月还利息，到期还本金。每月的还款金额X,融资金额A,年化收益B,总期数M,,每月天数D
 *      每月还款本金：0，最后一个月为A
 *      每月还款利息：A*B/360*本期天数
 *      X=每月还款本金+每月还款利息101
 */
@Service
class PayInterestSchedulePrincipalMaturityImpl : BaseRepaymentScheduleService {


    override fun calRepaymentSchedule(dtoRepaymentScheduleCalculateTrial: DTORepaymentScheduleCalculateTrial): DTORepaymentScheduleView {

        val amount = dtoRepaymentScheduleCalculateTrial.amount
        val interestRate = dtoRepaymentScheduleCalculateTrial.interestRate
        val repaymentFrequency = dtoRepaymentScheduleCalculateTrial.repaymentFrequency
        var repaymentDay = dtoRepaymentScheduleCalculateTrial.repaymentDay
        val startDate = dtoRepaymentScheduleCalculateTrial.startDate
        val endDate = dtoRepaymentScheduleCalculateTrial.endDate
        val baseYearDays = dtoRepaymentScheduleCalculateTrial.baseYearDays

        // 应还期数
        val periods = CalcPeriodComponent.calcDuePeriods(startDate, endDate, repaymentDay, repaymentFrequency)

        // 每期利息
        var interest: BigDecimal

        // 下一个还款日
        val maximumValue = startDate.toDateTime().dayOfMonth().maximumValue
        if (repaymentDay > maximumValue) {
            repaymentDay = maximumValue
        }
        var currentRepaymentDateTime = startDate.toDateTime()
        var nextRepaymentDateTime = DateTime(
            currentRepaymentDateTime.year,
            currentRepaymentDateTime.monthOfYear,
            repaymentDay,
            0,
            0
        ).plusMonths(repaymentFrequency.term.toMonthUnit().num)
        val finalRepaymentDateTime = endDate.toDateTime()

        // 每期还款详情
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailView> = ArrayList()
        for (i in 0 until periods) {

            nextRepaymentDateTime = CalcDateComponent.calcNextRepaymentDateTime(
                currentRepaymentDateTime,
                finalRepaymentDateTime,
                nextRepaymentDateTime
            )
            interest = CalculateInterest(amount, CalculateInterestRate(interestRate)).getDaysInterest(

                currentRepaymentDateTime,
                nextRepaymentDateTime,
                baseYearDays
            )

            // 计划明细
            dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailView(
                period = i + 1,
                installment = if (periods == i + 1) CalcInstallmentComponent.calcBaseRepaymentInstallment(
                    amount,
                    interest
                ) else interest.setScale(2, RoundingMode.HALF_UP),
                principal = if (periods == i + 1) amount else BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP),
                interest = interest.setScale(2, RoundingMode.HALF_UP),
                repaymentDate = nextRepaymentDateTime.toInstant(),
                remainPrincipal = if (periods == i + 1) BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) else amount
            )
            currentRepaymentDateTime = nextRepaymentDateTime
            nextRepaymentDateTime = nextRepaymentDateTime.plusMonths(1 * repaymentFrequency.term.toMonthUnit().num)
        }
        // 还款计划概述
        return DTORepaymentScheduleView(
            interestRate = interestRate.setScale(6, RoundingMode.HALF_UP),
            schedule = dtoRepaymentScheduleDetailTrialView
        )
    }

    override fun calResetRepaymentSchedule(dtoRepaymentScheduleResetCalculate: DTORepaymentScheduleResetCalculate): RepaymentSchedule {

        val remainLoanAmount = dtoRepaymentScheduleResetCalculate.remainLoanAmount
        val repaymentDate = dtoRepaymentScheduleResetCalculate.repaymentDate
        val baseYearDays = dtoRepaymentScheduleResetCalculate.baseYearDays
        val repaymentSchedule = dtoRepaymentScheduleResetCalculate.oldRepaymentSchedule
        val interestRate = dtoRepaymentScheduleResetCalculate.oldRepaymentSchedule.interestRate
        val repaymentScheduleDetail = repaymentSchedule.schedule

        // 每期还款详情
        val repaymentScheduleDetails: MutableList<RepaymentScheduleDetail> = ArrayList()

        // 剩余期数和当前期数
        val result = CalcPeriodComponent.calcRemainPeriods(repaymentDate.toInstant(), repaymentScheduleDetail)
        val currentPeriod = result[0]
        val finalPeriod = result[1]
        var currentRepaymentDateTime = DateTime(repaymentDate)

        // 每期详情
        for (detail in repaymentScheduleDetail) {
            if (currentPeriod <= detail.period) {
                val nextRepaymentDateTime = DateTime(detail.repaymentDate)
                // 每期利息
                val interest = CalculateInterest(remainLoanAmount, CalculateInterestRate(interestRate)).getDaysInterest(

                    currentRepaymentDateTime,
                    nextRepaymentDateTime,
                    baseYearDays                )

                detail.interest = interest
                detail.principal = if (finalPeriod == detail.period) remainLoanAmount else BigDecimal.ZERO.setScale(
                    2,
                    RoundingMode.HALF_UP
                )
                detail.installment = CalcInstallmentComponent.calcBaseRepaymentInstallment(detail.principal, interest)
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


























