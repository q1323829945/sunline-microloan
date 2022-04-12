package cn.sunline.saas.repayment.schedule.factory.impl

import cn.sunline.saas.repayment.schedule.component.*
import cn.sunline.saas.repayment.schedule.factory.BaseRepaymentScheduleCalculator
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
import cn.sunline.saas.repayment.schedule.model.dto.DTOInterestCalculator
import cn.sunline.saas.repayment.schedule.model.dto.DTOPrincipalCalculator
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleCalculate
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleResetCalculate
import cn.sunline.saas.repayment.schedule.model.enum.LoanRateType
import cn.sunline.saas.repayment.schedule.service.RepaymentScheduleDetailService
import cn.sunline.saas.repayment.schedule.service.RepaymentScheduleService
import cn.sunline.saas.seq.Sequence
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode


/**
 * 等额本金 每月还款额=贷款本金÷贷款期期数+（本金-已归还本金累计额）×月利率
 *        每季还款额=贷款本金÷贷款期季数+（本金-已归还本金累计额）×季利率
 */
@Service
class EqualPrincipalCalculator: BaseRepaymentScheduleCalculator {

    @Autowired
    private lateinit var seq: Sequence

    @Autowired
    private lateinit var repaymentScheduleService: RepaymentScheduleService

    @Autowired
    private lateinit var repaymentScheduleDetailService: RepaymentScheduleDetailService

    override fun calRepaymentSchedule(dtoRepaymentScheduleCalculate: DTORepaymentScheduleCalculate): RepaymentSchedule {

        val term = dtoRepaymentScheduleCalculate.term
        val paymentMethod = dtoRepaymentScheduleCalculate.paymentMethod
        val amount = dtoRepaymentScheduleCalculate.amount
        val interestRate = dtoRepaymentScheduleCalculate.interestRate
        val repaymentFrequency = dtoRepaymentScheduleCalculate.repaymentFrequency!!
        val repaymentDay = dtoRepaymentScheduleCalculate.repaymentDay
        val startDate = dtoRepaymentScheduleCalculate.startDate
        val endDate = dtoRepaymentScheduleCalculate.endDate
        val baseYearDays = dtoRepaymentScheduleCalculate.baseYearDays

        val repaymentScheduleId = seq.nextId()

        // 开始日期
        var currentRepaymentDateTime = DateTime(startDate)

        // 下一个还款日
        var nextRepaymentDateTime = DateTime(currentRepaymentDateTime.year, currentRepaymentDateTime.monthOfYear,repaymentDay,0,0).plusMonths(repaymentFrequency.ordinal)

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
        val repaymentScheduleDetails: MutableList<RepaymentScheduleDetail> = ArrayList()

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
            repaymentScheduleDetails += RepaymentScheduleDetail(
                id = seq.nextId(),
                repaymentScheduleId = repaymentScheduleId,
                period = i + 1,
                repaymentInstallment = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(principal,interest),
                principal = principal,
                interest = interest,
                repaymentDate = nextRepaymentDateTime.toInstant(),//org.joda.time.Instant.toDate(),//Instant.now(Clock.systemUTC()).toEpochMilli().toString(),//nextRepaymentDateTime.toDate(),
                remainPrincipal = remainPrincipal
            )

            currentRepaymentDateTime = nextRepaymentDateTime
            nextRepaymentDateTime = nextRepaymentDateTime.plusMonths(1 * repaymentFrequency.ordinal)
        }

        // 还款计划概述
        val totalRepayment = amount.add(totalInterest)
        return RepaymentSchedule(
            repaymentScheduleId = repaymentScheduleId,
            amount = amount,
            term = term,
            interestRate = interestRate.setScale(6, RoundingMode.HALF_UP),
            paymentMethod = paymentMethod,
            totalInterest = totalInterest,
            totalRepayment = totalRepayment,
            repaymentFrequency = repaymentFrequency,
            repaymentScheduleDetail = repaymentScheduleDetails
        )
    }

    override fun calResetRepaymentSchedule(
        dtoRepaymentScheduleResetCalculate: DTORepaymentScheduleResetCalculate
    ): RepaymentSchedule {

        val repaymentScheduleId = dtoRepaymentScheduleResetCalculate.repaymentScheduleId
        var remainLoanAmount = dtoRepaymentScheduleResetCalculate.remainLoanAmount
        val loanRate = dtoRepaymentScheduleResetCalculate.loanRate
        val repaymentDate = dtoRepaymentScheduleResetCalculate.repaymentDate
        val baseYearDays = dtoRepaymentScheduleResetCalculate.baseYearDays

        // 查询
        val repaymentSchedule = repaymentScheduleService.getOne(repaymentScheduleId)!!
        val repaymentScheduleDetail = repaymentSchedule.repaymentScheduleDetail
        repaymentScheduleDetail.sortBy { it.period }

        // 每期还款详情
        val repaymentScheduleDetails: MutableList<RepaymentScheduleDetail> = ArrayList()

        // 月利率
        val loanRateMonth = CalcRateComponent.calcBaseRate(loanRate, LoanRateType.MONTHLY, null)

        // 日利率
        val loanRateDay = CalcRateComponent.calcBaseRate(loanRate, LoanRateType.DAILY, baseYearDays)

        // 剩余期数和当前期数
        val result = CalcPeriodComponent.calcRemainPeriods(repaymentDate, repaymentScheduleDetail)
        val currentPeriod = result[0]
        val finalPeriod = result[1]
        val periods = result[2]

        // 每期应还本金
        var principal = CalcPrincipalComponent.calcBasePrincipal(
            DTOPrincipalCalculator(
                remainLoanAmount, repaymentSchedule.paymentMethod, BigDecimal.ZERO, periods, 0, null
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
                detail.remainPrincipal = remainLoanAmount
                detail.repaymentInstallment = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(principal, interest)

                // 日期滚动
                currentRepaymentDateTime = nextRepaymentDateTime
            }

            totalInterest = totalInterest.add(detail.interest)
            repaymentScheduleDetails += detail
        }
        repaymentSchedule.repaymentScheduleDetail = repaymentScheduleDetails
        repaymentSchedule.interestRate = loanRate.setScale(6, RoundingMode.HALF_UP)
        repaymentSchedule.totalInterest = totalInterest
        repaymentSchedule.totalRepayment = repaymentSchedule.amount.add(totalInterest)

        // 更新
        repaymentScheduleService.save(repaymentSchedule)
        repaymentScheduleDetailService.save(repaymentScheduleDetails)

        return repaymentSchedule
    }
}