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
import org.joda.time.Instant
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.collections.ArrayList


/**
 * 等额本息
 * 每月还款金额 = [总贷款金额 * 月利率 * （1 + 月利率） ^ 还款月数] / [(1 + 利率) ^ 总期数 -1]
 * 每期应还款额＝【借款本金×季度利率×（1＋季度利率）^还款期数】/【（1＋季度利率）^还款期数－1】
 */
@Service
class EqualInstallmentCalculator : BaseRepaymentScheduleCalculator {

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

        // 每期利率
        val loanRateMonth = CalcRateComponent.calcBaseRate(interestRate, LoanRateType.MONTHLY,null)

        // 日利率
        val loanRateDay = CalcRateComponent.calcBaseRate(interestRate,LoanRateType.DAILY,baseYearDays)

        // 应还期数
        val periods = CalcPeriodComponent.calcDuePeriods(startDate,endDate,repaymentDay,repaymentFrequency)

        // 当前日期
        var currentRepaymentDateTime = DateTime(startDate)

        // 下一个还款日
        var nextRepaymentDateTime = DateTime(currentRepaymentDateTime.year, currentRepaymentDateTime.monthOfYear, repaymentDay,0,0).plusMonths(repaymentFrequency.ordinal)

        // 结束日期
        val finalRepaymentDateTime = DateTime(endDate)

        // 平均还款金额
        val repaymentInstallment = CalcRepaymentInstallmentComponent.calcCapitalInstallment(amount,loanRateMonth,periods,repaymentFrequency)

        // 每期应还本金
        var principal: BigDecimal

        // 每期利息
        var interest : BigDecimal

        // 总利息
        var totalInterest = BigDecimal.ZERO

        // 剩余本金
        var remainPrincipal = amount

        // 每期还款详情
        val repaymentScheduleDetails: MutableList<RepaymentScheduleDetail> = ArrayList()
        for (i in 0 until periods) {

            // 还款日
            nextRepaymentDateTime = CalcDateComponent.calcNextRepaymentDateTime(currentRepaymentDateTime, finalRepaymentDateTime, nextRepaymentDateTime)

            // 首期或未期不足月先按日计算利息
            if(nextRepaymentDateTime.compareTo(currentRepaymentDateTime.plusMonths(1 * repaymentFrequency.ordinal)) != 0){
                interest = CalcInterestComponent.calcBaseInterest(DTOInterestCalculator(remainPrincipal,loanRateMonth,loanRateDay,currentRepaymentDateTime,nextRepaymentDateTime,repaymentFrequency))
                principal = repaymentInstallment.subtract(interest)
                if(remainPrincipal.subtract(principal) < BigDecimal.ZERO){
                    principal = principal.add(remainPrincipal.subtract(principal))
                }
            }else{
                // 本金计算
                principal = CalcPrincipalComponent.calcBasePrincipal(DTOPrincipalCalculator(amount,paymentMethod,loanRateMonth,periods,i+1,repaymentFrequency))
                if(remainPrincipal.subtract(principal) < BigDecimal.ZERO){
                    principal = principal.add(remainPrincipal.subtract(principal))
                }
                interest =  repaymentInstallment.subtract(principal)
            }

            remainPrincipal = remainPrincipal.subtract(principal)
            totalInterest = totalInterest.add(interest)

            // 计划明细
            repaymentScheduleDetails += RepaymentScheduleDetail(
                id = seq.nextId(),
                repaymentScheduleId = repaymentScheduleId,
                period =  i + 1,
                repaymentInstallment = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(principal,interest),
                principal = principal,
                interest = interest,
                repaymentDate = nextRepaymentDateTime.toInstant(),//nextRepaymentDateTime.toDate(),
                remainPrincipal = remainPrincipal
            )

            currentRepaymentDateTime = nextRepaymentDateTime
            nextRepaymentDateTime = nextRepaymentDateTime.plusMonths(1 * repaymentFrequency.ordinal)
        }

        // 还款总额
        val totalRepayment = amount.add(totalInterest)
        // 还款计划概述
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
        val loanRateMonth = CalcRateComponent.calcBaseRate(loanRate, LoanRateType.MONTHLY,null)
        // 日利率
        val loanRateDay = CalcRateComponent.calcBaseRate(loanRate, LoanRateType.DAILY,baseYearDays)
        // 频率
        val repaymentFrequency = repaymentSchedule.repaymentFrequency
        val paymentMethod = repaymentSchedule.paymentMethod
        // 剩余期数和当前期数
        val result = CalcPeriodComponent.calcRemainPeriods(repaymentDate,repaymentScheduleDetail)
        val currentPeriod = result[0]
        val periods = result[2]

        // 平均还款金额
        val repaymentInstallment = CalcRepaymentInstallmentComponent.calcCapitalInstallment(remainLoanAmount,loanRateMonth,periods,repaymentFrequency!!)
        val totalLoanAmount = remainLoanAmount
        var currentRepaymentDateTime = DateTime(repaymentDate)
        var totalInterest = BigDecimal.ZERO
        var i = 0
        for (detail in repaymentScheduleDetail) {
            if (currentPeriod <= detail.period) {
                val nextRepaymentDateTime = DateTime(detail.repaymentDate)
                var interest: BigDecimal
                var principal: BigDecimal
                // 首期或未期不足月先按日计算利息
                if(nextRepaymentDateTime.compareTo(currentRepaymentDateTime.plusMonths(1 * repaymentFrequency.ordinal)) != 0){
                    interest = CalcInterestComponent.calcBaseInterest(DTOInterestCalculator(remainLoanAmount,loanRateMonth,loanRateDay,currentRepaymentDateTime,nextRepaymentDateTime,repaymentFrequency))
                    principal = repaymentInstallment.subtract(interest)
                    if(remainLoanAmount.subtract(principal) < BigDecimal.ZERO){
                        principal = principal.add(remainLoanAmount.subtract(principal))
                    }
                }else{
                    // 本金计算
                    principal = CalcPrincipalComponent.calcBasePrincipal(DTOPrincipalCalculator(totalLoanAmount,paymentMethod,loanRateMonth,periods,i+1,repaymentFrequency))
                    if(remainLoanAmount.subtract(principal) < BigDecimal.ZERO){
                        principal = principal.add(remainLoanAmount.subtract(principal))
                    }
                    interest =  repaymentInstallment.subtract(principal)
                }
                remainLoanAmount = remainLoanAmount.subtract(principal)
                totalInterest = totalInterest.add(interest)


                detail.principal = principal
                detail.interest = interest
                detail.remainPrincipal = remainLoanAmount
                detail.repaymentInstallment = CalcRepaymentInstallmentComponent.calcBaseRepaymentInstallment(principal,interest)

                // 日期滚动
                currentRepaymentDateTime = nextRepaymentDateTime
                i++
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