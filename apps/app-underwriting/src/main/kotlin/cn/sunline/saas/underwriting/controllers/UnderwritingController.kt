package cn.sunline.saas.underwriting.controllers

import cn.sunline.saas.underwriting.controllers.dto.*
import cn.sunline.saas.underwriting.model.db.UnderwritingApplicationData
import cn.sunline.saas.underwriting.service.UnderwritingService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.dapr.Topic
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


    @Topic(name = "INITIATE_UNDERWRITING", pubsubName = "underwriting-pub-sub")
    @PostMapping("/Initiate")
    fun initiate(@RequestBody(required = false) dtoApplication: DTOLoanApplication): Mono<Unit> {
        return Mono.fromRunnable {
            integratedConfigurationService.initiate(
                objectMapper.convertValue<UnderwritingApplicationData>(
                    dtoApplication.data
                )
            )
        }
    }

    @Topic(name = "CALL_BACK_CUSTOMER_CREDIT_RATING", pubsubName = "underwriting-pub-sub")
    @PostMapping("/CustomerCreditRating")
    fun callBackCustomerCreditRating(@RequestBody(required = false) dtoCustomerCreditRating: DTOCustomerCreditRating): Mono<Unit> {
        return Mono.fromRunnable {
            integratedConfigurationService.updateCustomerCreditRating(
                dtoCustomerCreditRating.data.applId,
                dtoCustomerCreditRating.data.customerCreditRate
            )
        }
    }

    @Topic(name = "CALL_BACK_CREDIT_RISK", pubsubName = "underwriting-pub-sub")
    @PostMapping("/CreditRisk")
    fun callBackCreditRisk(@RequestBody(required = false) dtoCreditRisk: DTOCreditRisk): Mono<Unit> {
        return Mono.fromRunnable {
            integratedConfigurationService.updateCreditRisk(
                dtoCreditRisk.data.applId,
                dtoCreditRisk.data.creditRisk
            )
        }
    }

    @Topic(name = "CALL_BACK_REGULATORY_COMPLIANCE", pubsubName = "underwriting-pub-sub")
    @PostMapping("/RegulatoryCompliance")
    fun callBackRegulatoryCompliance(@RequestBody(required = false) dtoRegulatoryCompliance: DTORegulatoryCompliance): Mono<Unit> {
        return Mono.fromRunnable {
            integratedConfigurationService.updateRegulatoryCompliance(
                dtoRegulatoryCompliance.data.applId,
                dtoRegulatoryCompliance.data.regulatoryCompliance
            )
        }
    }

    @Topic(name = "CALL_BACK_CUSTOMER_FRAUD_EVALUATION", pubsubName = "underwriting-pub-sub")
    @PostMapping("/FraudEvaluation")
    fun callBackFraudEvaluation(@RequestBody(required = false) dtoFraudEvaluation: DTOFraudEvaluation): Mono<Unit> {
        return Mono.fromRunnable {
            integratedConfigurationService.updateFraudEvaluation(
                dtoFraudEvaluation.data.applId,
                dtoFraudEvaluation.data.fraudEvaluation
            )
        }
    }
}