package cn.sunline.saas.loan.controller

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.loan.dto.DTORepaymentPlanView
import cn.sunline.saas.loan.service.LoanService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("loan")
class LoanController {

    @Autowired
    private lateinit var loanService: LoanService

    @GetMapping("{productId}/{amount}/{term}/calculate")
    fun calculate(@PathVariable("productId") productId:Long,@PathVariable("amount") amount:String,@PathVariable term: LoanTermType):ResponseEntity<DTOResponseSuccess<DTORepaymentPlanView>>{
        val dtoRepaymentPlanView = loanService.getLoanCalculate(productId, amount, term.days.toString())
        return DTOResponseSuccess(dtoRepaymentPlanView).response()
    }
}