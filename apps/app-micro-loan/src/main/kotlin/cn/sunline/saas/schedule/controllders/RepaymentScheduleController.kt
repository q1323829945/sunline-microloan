package cn.sunline.saas.schedule.controllders

import cn.sunline.saas.formula.constant.CalculatePrecision
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleView
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.schedule.Schedule
import cn.sunline.saas.schedule.dto.DTORepaymentScheduleDetailTrialView
import cn.sunline.saas.schedule.dto.DTORepaymentScheduleTrialView
import cn.sunline.saas.schedule.service.ConsumerRepaymentScheduleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @title: RepaymentScheduleController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 13:50
 */
@RestController
@RequestMapping("/ConsumerLoan")
class RepaymentScheduleController {

    @Autowired
    private lateinit var consumerRepaymentScheduleService: ConsumerRepaymentScheduleService

    @GetMapping("{productId}/{amount}/{term}/calculate")
    fun calculate(@PathVariable productId:Long, @PathVariable amount:String, @PathVariable term: LoanTermType): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleTrialView>> {
        val repaymentScheduleResult = consumerRepaymentScheduleService.calculate(productId, amount.toBigDecimal(),term)
        val repaymentScheduleTrialResult = changeMapper(repaymentScheduleResult)
        return DTOResponseSuccess(repaymentScheduleTrialResult).response()
    }


    private fun changeMapper(dtoSchedule:  MutableList<Schedule>): DTORepaymentScheduleTrialView {
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailTrialView> = ArrayList()

        val interestRate = dtoSchedule.first().interestRate
        var installment = dtoSchedule.first().instalment
        for (schedule in dtoSchedule) {
            if(installment != schedule.instalment){
                installment = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
            }
            dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailTrialView(
                period = schedule.period,
                installment = schedule.instalment,
                principal = schedule.principal,
                interest = schedule.interest,
                repaymentDate = CalcDateComponent.formatInstantToView(schedule.dueDate)
            )
        }
        return DTORepaymentScheduleTrialView(
            installment = installment,
            interestRate = interestRate,
            schedule = dtoRepaymentScheduleDetailTrialView
        )
    }
}