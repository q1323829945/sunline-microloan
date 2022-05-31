package cn.sunline.saas.repayment.schedule.factory.impl

import cn.sunline.saas.formula.CalculateInterest
import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.component.CalcInstallmentComponent
import cn.sunline.saas.repayment.schedule.factory.BaseRepaymentScheduleService
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculateTrial
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleDetailView
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleView
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 到期还本还息
 *  到期一次还本付息额=贷款本金×[1+日利率×贷款天数]
 *  日利率 = 年利率 / 360
 */
@Service
class OneOffRepaymentImpl : BaseRepaymentScheduleService {


    override fun calRepaymentSchedule(dtoRepaymentScheduleCalculateTrial: DTORepaymentScheduleCalculateTrial): DTORepaymentScheduleView {

        val amount = dtoRepaymentScheduleCalculateTrial.amount
        val interestRate = dtoRepaymentScheduleCalculateTrial.interestRate
        val startDate = dtoRepaymentScheduleCalculateTrial.startDate
        val endDate = dtoRepaymentScheduleCalculateTrial.endDate
        val baseYearDays = dtoRepaymentScheduleCalculateTrial.baseYearDays

        // 下一个还款日
        val currentRepaymentDateTime = startDate.toDateTime()
        var nextRepaymentDateTime = endDate.toDateTime()
        val finalRepaymentDateTime = endDate.toDateTime()

        // 每期还款详情
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailView> = ArrayList()

        nextRepaymentDateTime = CalcDateComponent.calcNextRepaymentDateTime(
            currentRepaymentDateTime,
            finalRepaymentDateTime,
            nextRepaymentDateTime
        )

        // 每期利息
        val interest = CalculateInterest(amount, CalculateInterestRate(interestRate)).getDaysInterest(
            currentRepaymentDateTime,
            nextRepaymentDateTime,
            baseYearDays
        )

        // 计划明细
        dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailView(
            period = 1,
            installment = CalcInstallmentComponent.calcBaseRepaymentInstallment(amount, interest),
            principal = amount,
            interest = interest,
            repaymentDate = nextRepaymentDateTime.toInstant(),
            remainPrincipal = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
        )
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

        for (detail in repaymentScheduleDetail) {
            val nextRepaymentDateTime = detail.repaymentDate.toInstant()!!
            // 每期利息
            val interest = CalculateInterest(remainLoanAmount, CalculateInterestRate(interestRate)).getDaysInterest(
                repaymentDate,
                repaymentDate, baseYearDays
            )

            detail.interest = interest
            detail.principal = remainLoanAmount
            detail.installment = CalcInstallmentComponent.calcBaseRepaymentInstallment(detail.principal, interest)
            repaymentScheduleDetails += detail
        }
        repaymentSchedule.schedule = repaymentScheduleDetails
        repaymentSchedule.interestRate = interestRate
        return repaymentSchedule
    }
}











