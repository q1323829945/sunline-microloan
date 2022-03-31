

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
 * 到期还本还息
 *  到期一次还本付息额=贷款本金×[1+日利率×贷款天数]
 *  日利率 = 年利率 / 360
 */
@Service
class OneOffRepaymentCalculator : BaseRepaymentScheduleCalculator {

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

        // 日利率
        val loanRateDay = CalcRateComponent.calcBaseRate(interestRate, LoanRateType.DAILY,baseYearDays)

        // 总利息
        var totalInterest = BigDecimal.ZERO

        // 下一个还款日
        val currentRepaymentDateTime = DateTime(startDate)
        var nextRepaymentDateTime = DateTime(endDate)
        val finalRepaymentDateTime = DateTime(endDate)

        // 每期还款详情
        val repaymentScheduleDetails: MutableList<RepaymentScheduleDetail> = ArrayList()

        nextRepaymentDateTime = CalcDateComponent.calcNextRepaymentDateTime(currentRepaymentDateTime,finalRepaymentDateTime,nextRepaymentDateTime)

        val interest = CalcInterestComponent.calcDayInterest(amount,loanRateDay,currentRepaymentDateTime,nextRepaymentDateTime)
        totalInterest = totalInterest.add(interest)
        // 计划明细
        repaymentScheduleDetails += RepaymentScheduleDetail(
            id = seq.nextId(),
            repaymentScheduleId = repaymentScheduleId,
            period = 1,
            repaymentInstallment = interest.add(amount),
            principal = amount,
            interest = interest,
            repaymentDate = nextRepaymentDateTime.toInstant(),//nextRepaymentDateTime.toDate(),
            remainPrincipal =  BigDecimal.ZERO.setScale(2,RoundingMode.HALF_UP)
        )

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
        var totalInterest = BigDecimal.ZERO
        val currentRepaymentDateTime = DateTime(repaymentDate)
        for (detail in repaymentScheduleDetail) {
            val nextRepaymentDateTime = DateTime(detail.repaymentDate)
            // 每期利息
            val interest = CalcInterestComponent.calcDayInterest(remainLoanAmount,loanRateDay,currentRepaymentDateTime,nextRepaymentDateTime)
            detail.interest = interest
            detail.principal = remainLoanAmount
            detail.repaymentInstallment = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(detail.principal,interest)
            detail.remainPrincipal =  BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
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











