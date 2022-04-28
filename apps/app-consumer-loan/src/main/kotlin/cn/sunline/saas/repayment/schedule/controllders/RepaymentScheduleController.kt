package cn.sunline.saas.repayment.schedule.controllders

import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.repayment.schedule.invoke.LoanProductDirectoryService
import cn.sunline.saas.repayment.schedule.service.RepaymentScheduleService
import cn.sunline.saas.response.DTOResponseSuccess
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
    private lateinit var repaymentScheduleService: RepaymentScheduleService

//    @Autowired
//    private lateinit var loanProductDirectoryService: LoanProductDirectoryService

    /*
    @GetMapping("{productId}/{amount}/{term}/calculate")
    fun calculate(@PathVariable productId:Long, @PathVariable amount:String, @PathVariable term:Int): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleView>> {
        return repaymentScheduleService.calculate(productId, amount.toBigDecimal(),term)
    }
    */
    @GetMapping("product/{productId}")
    fun getInfo(@PathVariable productId:Long): ResponseEntity<DTOResponseSuccess<DTOLoanProductView>> {
        return repaymentScheduleService.getInfo(productId)
    }
}