package cn.sunline.saas.underwriting.controllers

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.underwriting.controllers.dto.*
import cn.sunline.saas.underwriting.model.db.UnderwritingApplicationData
import cn.sunline.saas.underwriting.service.UnderwritingService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

/**
 * @title: UnderwritingController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/7 15:01
 */
@RestController
@RequestMapping("Underwriting")
class UnderwritingController(private val integratedConfigurationService: UnderwritingService) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @PostMapping("/Initiate")
    fun initiate(@RequestBody(required = false) dtoApplication: DTOLoanApplicationData): Mono<Unit> {
        return Mono.fromRunnable {
            integratedConfigurationService.initiate(
                objectMapper.convertValue<UnderwritingApplicationData>(
                    dtoApplication
                )
            )
        }
    }

    @PostMapping("/CustomerCreditRating")
    fun callBackCustomerCreditRating(@RequestBody(required = false) dtoCustomerCreditRating: DTOCustomerCreditRating): Mono<Unit> {
        return Mono.fromRunnable {
            integratedConfigurationService.updateCustomerCreditRating(
                dtoCustomerCreditRating.applId,
                dtoCustomerCreditRating.customerCreditRate
            )
        }
    }

    @PostMapping("/CreditRisk")
    fun callBackCreditRisk(@RequestBody(required = false) dtoCreditRisk: DTOCreditRisk): Mono<Unit> {
        return Mono.fromRunnable {
            integratedConfigurationService.updateCreditRisk(
                dtoCreditRisk.applId,
                dtoCreditRisk.creditRisk
            )
        }
    }

    @PostMapping("/RegulatoryCompliance")
    fun callBackRegulatoryCompliance(@RequestBody(required = false) dtoRegulatoryCompliance: DTORegulatoryCompliance): Mono<Unit> {
        return Mono.fromRunnable {
            integratedConfigurationService.updateRegulatoryCompliance(
                dtoRegulatoryCompliance.applId,
                dtoRegulatoryCompliance.regulatoryCompliance
            )
        }
    }

    @PostMapping("/FraudEvaluation")
    fun callBackFraudEvaluation(@RequestBody(required = false) dtoFraudEvaluation: DTOFraudEvaluation): Mono<Unit> {
        return Mono.fromRunnable {
            integratedConfigurationService.updateFraudEvaluation(
                dtoFraudEvaluation.applId,
                dtoFraudEvaluation.fraudEvaluation
            )
        }
    }
    data class Test(
        val id:String,
        val name:String
    )

    @GetMapping("test2")
    fun test2(){
        val t = Test("1","publish")
        DaprHelper.publish("underwriting-pub-sub","RETRIEVE_CUSTOMER_CREDIT_RATING",t)
    }

    @GetMapping("test3")
    fun test3(){
        val t = Test("1","bindingRabbitMq")
        DaprHelper.binding("mytest3","create",t)
    }
}