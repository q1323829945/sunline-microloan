package cn.sunline.saas.credit.risk.wrapper.controllers

import cn.sunline.saas.credit.risk.wrapper.dto.DTOCreditRisk
import cn.sunline.saas.credit.risk.wrapper.service.CreditRiskService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("CreditRisk")
class CreditRiskController {
    @Autowired
    private lateinit var creditRiskService: CreditRiskService

    @PostMapping
    fun getCreditRisk(@RequestBody(required = false) dtoCreditRisk: DTOCreditRisk): Mono<Unit> {
        println(dtoCreditRisk)
        return Mono.fromRunnable(){
            creditRiskService.getCreditRisk(dtoCreditRisk)
        }
    }
}