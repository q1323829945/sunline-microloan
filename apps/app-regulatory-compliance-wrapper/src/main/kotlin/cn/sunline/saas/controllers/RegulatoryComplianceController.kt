package cn.sunline.saas.controllers

import cn.sunline.saas.dto.DTORegulatoryCompliance
import cn.sunline.saas.service.RegulatoryComplianceService
import io.dapr.Topic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * @title: CustomerOfferProcedureController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 14:25
 */
@RestController
@RequestMapping("CreditRating")
class RegulatoryComplianceController {
    @Autowired
    private lateinit var regulatoryComplianceService: RegulatoryComplianceService


    @Topic(name = "RETRIEVE_CUSTOMER_CREDIT_RATING", pubsubName = "underwriting-pub-sub")
    @PostMapping
    fun getRegulatoryCompliance(@RequestBody(required = false) dtoRegulatoryCompliance: DTORegulatoryCompliance): Mono<Unit> {
        //TODO:
        println(dtoRegulatoryCompliance)
        return Mono.fromRunnable(){
            regulatoryComplianceService.getRegulatoryCompliance(dtoRegulatoryCompliance)
        }
    }
}