
















package cn.sunline.saas.repayment.schedule.factory.impl

import cn.sunline.saas.repayment.schedule.component.*
import cn.sunline.saas.repayment.schedule.factory.BaseRepaymentScheduleCalculator
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.db.RepaymentScheduleDetail
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
import org.joda.time.Instant
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
        val repaymentDayType = dtoRepaymentScheduleCalculate.repaymentDayType

        val repaymentScheduleId = seq.nextId()

        // 应还期数
        val periods = CalcPeriodComponent.calcDuePeriods(startDate,endDate,repaymentDay,repaymentFrequency)

        // 日利率
        val loanRateDay = CalcRateComponent.calcBaseRate(interestRate, LoanRateType.DAILY,baseYearDays)

        // 每期利息
        var interest: BigDecimal

        // 总利息
        var totalInterest = BigDecimal.ZERO

        // 下一个还款日
        var currentRepaymentDateTime = DateTime(startDate)
        var nextRepaymentDateTime = DateTime(currentRepaymentDateTime.year, currentRepaymentDateTime.monthOfYear, repaymentDay,0,0).plusMonths(repaymentFrequency.getMonths())
        val finalRepaymentDateTime = DateTime(endDate)


        // 每期还款详情
        val repaymentScheduleDetails: MutableList<RepaymentScheduleDetail> = java.util.ArrayList()
        for (i in 0 until periods) {

            nextRepaymentDateTime = CalcDateComponent.calcNextRepaymentDateTime(currentRepaymentDateTime, finalRepaymentDateTime, nextRepaymentDateTime)
            interest = CalcInterestComponent.calcDayInterest(amount,loanRateDay,currentRepaymentDateTime,nextRepaymentDateTime)
            totalInterest = totalInterest.add(interest)
            // 计划明细
            repaymentScheduleDetails += RepaymentScheduleDetail(
                id = seq.nextId(),
                repaymentScheduleId = repaymentScheduleId,
                period = i + 1,
                repaymentInstallment = if (periods == i + 1) interest.add(amount) else interest.setScale(2,RoundingMode.HALF_UP),
                principal = if(periods == i + 1) amount else BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP),
                interest = interest.setScale(2,RoundingMode.HALF_UP),
                repaymentDate = nextRepaymentDateTime.toInstant(),//nextRepaymentDateTime.toDate(),
                remainPrincipal =  if(periods == i + 1) BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP) else amount
            )

            currentRepaymentDateTime = nextRepaymentDateTime
            nextRepaymentDateTime = nextRepaymentDateTime.plusMonths(1 * repaymentFrequency.getMonths())
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
        val remainLoanAmount = dtoRepaymentScheduleResetCalculate.remainLoanAmount
        val loanRate = dtoRepaymentScheduleResetCalculate.loanRate
        val repaymentDate = dtoRepaymentScheduleResetCalculate.repaymentDate
        val baseYearDays = dtoRepaymentScheduleResetCalculate.baseYearDays

        // 查询
        val repaymentSchedule = repaymentScheduleService.getOne(repaymentScheduleId)!!
        val repaymentScheduleDetail = repaymentSchedule.repaymentScheduleDetail
        repaymentScheduleDetail.sortBy { it.period }

        // 每期还款详情
        val repaymentScheduleDetails: MutableList<RepaymentScheduleDetail> = ArrayList()

        // 日利率
        val loanRateDay = CalcRateComponent.calcBaseRate(loanRate, LoanRateType.DAILY,baseYearDays)
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
                detail.repaymentInstallment = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(detail.principal,interest)
                detail.remainPrincipal = if(finalPeriod == detail.period) BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP) else remainLoanAmount
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


























