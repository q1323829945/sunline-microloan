package cn.sunline.saas.underwriting.controllers

import cn.sunline.saas.underwriting.controllers.dto.DTOCustomerCreditRating
import cn.sunline.saas.underwriting.controllers.dto.DTOLoanApplicationData
import cn.sunline.saas.underwriting.model.db.UnderwritingApplicationData
import cn.sunline.saas.underwriting.service.UnderwritingService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
    fun initiate(@RequestBody(required = false) dtoApplicationData: DTOLoanApplicationData): Mono<Unit> {
        return Mono.fromRunnable {
            integratedConfigurationService.initiate(
                objectMapper.convertValue<UnderwritingApplicationData>(
                    dtoApplicationData
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
}