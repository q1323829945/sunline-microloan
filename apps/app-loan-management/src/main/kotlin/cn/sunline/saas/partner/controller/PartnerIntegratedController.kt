package cn.sunline.saas.partner.controller

import cn.sunline.saas.partner.integrated.model.dto.DTOPartnerIntegrated
import cn.sunline.saas.partner.integrated.service.PartnerIntegratedService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("PartnerIntegrated")
class PartnerIntegratedController(private val partnerIntegratedService: PartnerIntegratedService) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @PostMapping("/Initiate")
    fun initiate(@RequestBody dtoIntegratedConfiguration: DTOPartnerIntegrated): ResponseEntity<DTOResponseSuccess<DTOPartnerIntegrated>> {
        val integratedConfiguration = partnerIntegratedService.registered(
            objectMapper.convertValue(dtoIntegratedConfiguration)
        )
        return DTOResponseSuccess(
            objectMapper.convertValue<DTOPartnerIntegrated>(integratedConfiguration)
        ).response()
    }

    @GetMapping("/Retrieve")
    fun retrieve(): DTOPartnerIntegrated? {
        val integratedConfiguration = partnerIntegratedService.get() ?: Unit
        return objectMapper.convertValue<DTOPartnerIntegrated>(integratedConfiguration)
    }

}