package cn.sunline.saas.repayment.schedule.factory.impl

import cn.sunline.saas.formula.CalculateInterest
import cn.sunline.saas.formula.CalculateInterestRate
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.component.CalcInstallmentComponent
import cn.sunline.saas.repayment.schedule.component.CalcPeriodComponent
import cn.sunline.saas.repayment.schedule.factory.BaseRepaymentScheduleService
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.*
import org.joda.time.DateTime
import org.springframework.stereotype.Service
import java.math.BigDecimal


/**
 * 等额本息
 * 每月还款金额 = [总贷款金额 * 月利率 * （1 + 月利率） ^ 还款月数] / [(1 + 利率) ^ 总期数 -1]
 * 每期应还款额＝【借款本金×季度利率×（1＋季度利率）^还款期数】/【（1＋季度利率）^还款期数－1】
 */
@Service
class EqualInstallmentImpl : BaseRepaymentScheduleService {

    override fun calRepaymentSchedule(dtoRepaymentScheduleCalculateTrial: DTORepaymentScheduleCalculateTrial):DTORepaymentScheduleView {

        val paymentMethod = dtoRepaymentScheduleCalculateTrial.paymentMethod
        val amount = dtoRepaymentScheduleCalculateTrial.amount
        val interestRate = dtoRepaymentScheduleCalculateTrial.interestRate
        val repaymentFrequency = dtoRepaymentScheduleCalculateTrial.repaymentFrequency
        var repaymentDay = dtoRepaymentScheduleCalculateTrial.repaymentDay
        val startDate = dtoRepaymentScheduleCalculateTrial.startDate
        val endDate = dtoRepaymentScheduleCalculateTrial.endDate
        val baseYearDays = dtoRepaymentScheduleCalculateTrial.baseYearDays

        // 每期利率
        val loanRateMonth = CalculateInterestRate(interestRate).toMonthRate()

        // 应还期数
        val periods = CalcPeriodComponent.calcDuePeriods(startDate, endDate, repaymentDay, repaymentFrequency)

        // 当前日期
        var currentRepaymentDateTime = startDate.toDateTime()

        // 下一个还款日
        val maximumValue = startDate.toDateTime().dayOfMonth().maximumValue
        if (repaymentDay > maximumValue) {
            repaymentDay = maximumValue
        }
        var nextRepaymentDateTime = DateTime(
            currentRepaymentDateTime.year,
            currentRepaymentDateTime.monthOfYear,
            repaymentDay,
            0,
            0
        ).plusMonths(repaymentFrequency.term.toMonthUnit().num)

        // 结束日期
        val finalRepaymentDateTime = endDate.toDateTime()

        // 平均还款金额
        val repaymentInstallment =
            CalcInstallmentComponent.calcCapitalInstallment(amount, loanRateMonth, periods, repaymentFrequency)

        // 每期应还本金
        var principal: BigDecimal

        // 每期利息
        var interest: BigDecimal

        // 总利息
        var totalInterest = BigDecimal.ZERO

        // 剩余本金
        var remainPrincipal = amount

        // 每期还款详情
        var nextInstallment = BigDecimal.ZERO

        // 还款明细
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailView> = ArrayList()
        for (i in 0 until periods) {

            // 还款日
            nextRepaymentDateTime = CalcDateComponent.calcNextRepaymentDateTime(
                currentRepaymentDateTime,
                finalRepaymentDateTime,
                nextRepaymentDateTime
            )

            // 首期或未期不足月先按日计算利息
            if (nextRepaymentDateTime.compareTo(currentRepaymentDateTime.plusMonths(1 * repaymentFrequency.term.toMonthUnit().num)) != 0) {
                interest = CalculateInterest(remainPrincipal, CalculateInterestRate(interestRate)).getDaysInterest(
                    currentRepaymentDateTime,
                    nextRepaymentDateTime,
                    baseYearDays
                )
                principal = repaymentInstallment.subtract(interest)
                if (remainPrincipal.subtract(principal) < BigDecimal.ZERO) {
                    principal = principal.add(remainPrincipal.subtract(principal))
                }
            } else {
                // 本金计算
                principal = CalcInstallmentComponent.calcBasePrincipal(
                    DTOPrincipalCalculator(
                        amount,
                        paymentMethod,
                        loanRateMonth,
                        periods,
                        i + 1,
                        repaymentFrequency
                    )
                )
                if (remainPrincipal.subtract(principal) < BigDecimal.ZERO) {
                    principal = principal.add(remainPrincipal.subtract(principal))
                }
                interest = repaymentInstallment.subtract(principal)
            }

            remainPrincipal = remainPrincipal.subtract(principal)
            totalInterest = totalInterest.add(interest)
            nextInstallment = CalcInstallmentComponent.calcBaseRepaymentInstallment(principal, interest)
            // 计划明细
            dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailView(
                period = i + 1,
                principal = principal,
                interest = interest,
                repaymentDate = nextRepaymentDateTime.toInstant(),
                remainPrincipal = remainPrincipal,
                installment = nextInstallment
            )

            currentRepaymentDateTime = nextRepaymentDateTime
            nextRepaymentDateTime = nextRepaymentDateTime.plusMonths(1 * repaymentFrequency.term.toMonthUnit().num)
        }

        // 还款总额
        // 还款计划概述
        return DTORepaymentScheduleView(
            interestRate = interestRate,
            installment = nextInstallment,
            schedule = dtoRepaymentScheduleDetailTrialView
        )
    }

    override fun calResetRepaymentSchedule(dtoRepaymentScheduleResetCalculate: DTORepaymentScheduleResetCalculate): RepaymentSchedule {

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

        // 每期利率
        val loanRateMonth = CalculateInterestRate(interestRate).toMonthRate()

        // 剩余期数和当前期数
        val result = CalcPeriodComponent.calcRemainPeriods(repaymentDate.toInstant(), repaymentScheduleDetail)
        val currentPeriod = result[0]
        val periods = result[2]

        // 平均还款金额
        val repaymentInstallment = CalcInstallmentComponent.calcCapitalInstallment(
            remainLoanAmount,
            loanRateMonth,
            periods,
            repaymentFrequency
        )
        val totalLoanAmount = remainLoanAmount
        var totalInterest = BigDecimal.ZERO
        var currentRepaymentDateTime = repaymentDate
        var i = 0
        for (detail in repaymentScheduleDetail) {
            if (currentPeriod <= detail.period) {
                val nextRepaymentDateTime = DateTime(detail.repaymentDate)
                var interest: BigDecimal
                var principal: BigDecimal
                // 首期或未期不足月先按日计算利息
                if (nextRepaymentDateTime.compareTo(currentRepaymentDateTime.plusMonths(1 * repaymentFrequency.term.toMonthUnit().num)) != 0) {
                    interest = CalculateInterest(remainLoanAmount, CalculateInterestRate(interestRate)).getDaysInterest(
                            currentRepaymentDateTime,
                            nextRepaymentDateTime,
                            baseYearDays
                        )
                    principal = repaymentInstallment.subtract(interest)
                    if (remainLoanAmount.subtract(principal) < BigDecimal.ZERO) {
                        principal = principal.add(remainLoanAmount.subtract(principal))
                    }
                } else {
                    // 本金计算
                    principal = CalcInstallmentComponent.calcBasePrincipal(
                        DTOPrincipalCalculator(
                            totalLoanAmount,
                            paymentMethod,
                            loanRateMonth,
                            periods,
                            i + 1,
                            repaymentFrequency
                        )
                    )
                    if (remainLoanAmount.subtract(principal) < BigDecimal.ZERO) {
                        principal = principal.add(remainLoanAmount.subtract(principal))
                    }
                    interest = repaymentInstallment.subtract(principal)
                }
                remainLoanAmount = remainLoanAmount.subtract(principal)
                totalInterest = totalInterest.add(interest)


                detail.principal = principal
                detail.interest = interest

                // 日期滚动
                currentRepaymentDateTime = nextRepaymentDateTime
                i++
            }

            totalInterest = totalInterest.add(detail.interest)
            repaymentScheduleDetails += detail
        }
        repaymentSchedule.schedule = repaymentScheduleDetails
        repaymentSchedule.interestRate = interestRate
        return repaymentSchedule
    }
}
