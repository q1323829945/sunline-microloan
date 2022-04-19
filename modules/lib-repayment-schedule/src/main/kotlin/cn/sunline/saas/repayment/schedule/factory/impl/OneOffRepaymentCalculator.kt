

package cn.sunline.saas.repayment.schedule.factory.impl

import cn.sunline.saas.interest.util.InterestRateUtil
import cn.sunline.saas.interest.util.InterestUtil
import cn.sunline.saas.repayment.schedule.component.*
import cn.sunline.saas.repayment.schedule.factory.BaseRepaymentScheduleCalculator
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculateTrial
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleDetailTrialView
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleTrialView
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

        val amount = dtoRepaymentScheduleCalculateTrial.amount
        val interestRate = dtoRepaymentScheduleCalculateTrial.interestRate
        val startDate = dtoRepaymentScheduleCalculateTrial.startDate
        val endDate = dtoRepaymentScheduleCalculateTrial.endDate
        val baseYearDays = dtoRepaymentScheduleCalculateTrial.baseYearDays

        // 日利率
        val loanRateDay = InterestRateUtil.toDayRate(baseYearDays,interestRate)

        // 下一个还款日
        val currentRepaymentDateTime = startDate.toDateTime()
        var nextRepaymentDateTime = endDate.toDateTime()
        val finalRepaymentDateTime = endDate.toDateTime()

        // 每期还款详情
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailTrialView> = ArrayList()

        nextRepaymentDateTime = CalcDateComponent.calcNextRepaymentDateTime(currentRepaymentDateTime,finalRepaymentDateTime,nextRepaymentDateTime)

        // 每期利息
        val interest = InterestUtil.calDaysInterest(
            amount,
            loanRateDay,
            currentRepaymentDateTime.toInstant(),
            nextRepaymentDateTime.toInstant()
        )

        // 计划明细
        dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailTrialView(
            period = 1,
            installment = CalcInstallmentComponent.calcBaseRepaymentInstallment(amount,interest),
            principal = amount,
            interest = interest,
            repaymentDate = CalcDateComponent.formatInstantToView(nextRepaymentDateTime.toInstant()),
            remainPrincipal =  BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP)
        )
        return DTORepaymentScheduleTrialView(
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

        // 日利率
        val loanRateDay = InterestRateUtil.toDayRate(baseYearDays,interestRate)

        for (detail in repaymentScheduleDetail) {
            val nextRepaymentDateTime = detail.repaymentDate
            // 每期利息
            val interest = InterestUtil.calDaysInterest(
                remainLoanAmount,
                loanRateDay,
                repaymentDate,
                nextRepaymentDateTime
            )
            detail.interest = interest
            detail.principal = remainLoanAmount
            detail.installment = CalcInstallmentComponent.calcBaseRepaymentInstallment(detail.principal,interest)
            repaymentScheduleDetails += detail
        }
        repaymentSchedule.schedule = repaymentScheduleDetails
        repaymentSchedule.interestRate = interestRate
        return repaymentSchedule
    }
}











