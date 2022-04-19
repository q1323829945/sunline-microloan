package cn.sunline.saas.loan.controllers

import cn.sunline.saas.loan.dto.DTOLoanApplication
import cn.sunline.saas.repayment.schedule.model.dto.DTORepaymentScheduleView
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.loan.service.ConsumerRepaymentScheduleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * @title: ConsumerLoanController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 14:09
 */
@RestController
@RequestMapping("/ConsumerLoan")
class ConsumerLoanController {

    @Autowired
    private lateinit var consumerRepaymentScheduleService: ConsumerRepaymentScheduleService

    @PostMapping("/Initiate")
    fun initiate(dtoLoanApplication: DTOLoanApplication){
    }


    @GetMapping("{productId}/{amount}/{term}/calculate")
    fun calculate(@PathVariable productId:Long,@PathVariable amount:String,@PathVariable term:Int): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>> {
        return consumerRepaymentScheduleService.calculate(productId, amount.toBigDecimal(),term)
    }
}