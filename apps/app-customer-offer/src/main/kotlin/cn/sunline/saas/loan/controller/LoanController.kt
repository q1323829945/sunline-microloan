package cn.sunline.saas.loan.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("loan")
class LoanController {

    @GetMapping("{productId}/{amount}/{term}/calculate")
    fun calculate(@PathVariable("productId") productId:Long,@PathVariable("amount") amount:String,@PathVariable term:String){
        //TODO 还款计划
    }
}