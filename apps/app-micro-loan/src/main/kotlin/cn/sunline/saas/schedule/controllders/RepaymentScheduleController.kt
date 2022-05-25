package cn.sunline.saas.schedule.controllders

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.repayment.schedule.component.CalcDateComponent
import cn.sunline.saas.repayment.schedule.model.db.RepaymentSchedule
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleDetailView
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleView
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.schedule.dto.DTORepaymentScheduleDetailTrialView
import cn.sunline.saas.schedule.dto.DTORepaymentScheduleTrialView
import cn.sunline.saas.schedule.service.ConsumerRepaymentScheduleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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


    private fun changeMapper(dtoRepaymentScheduleView: DTORepaymentScheduleView): DTORepaymentScheduleTrialView {
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailTrialView> = ArrayList()
        for (schedule in dtoRepaymentScheduleView.schedule) {
            dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailTrialView(
                period = schedule.period,
                installment = schedule.installment,
                principal = schedule.principal,
                interest = schedule.interest,
                repaymentDate = CalcDateComponent.formatInstantToView(schedule.repaymentDate)
            )
        }
        return DTORepaymentScheduleTrialView(
            installment = dtoRepaymentScheduleView.installment,
            interestRate = dtoRepaymentScheduleView.interestRate,
            schedule = dtoRepaymentScheduleDetailTrialView
        )
    }
}