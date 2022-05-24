package cn.sunline.saas.schedule.controllders

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleTrialView
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.schedule.service.ConsumerRepaymentScheduleImpl
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

//    @Autowired
//    private lateinit var loanProductDirectoryService: LoanProductDirectoryService


    @GetMapping("{productId}/{amount}/{term}/calculate")
    fun calculate(@PathVariable productId:Long, @PathVariable amount:String, @PathVariable term: LoanTermType): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleTrialView>> {
        val calculate = consumerRepaymentScheduleService.calculate(productId, amount.toBigDecimal(),term)
        return DTOResponseSuccess(calculate).response()
    }

}