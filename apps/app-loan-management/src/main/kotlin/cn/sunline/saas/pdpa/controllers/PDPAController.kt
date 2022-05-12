package cn.sunline.saas.pdpa.controllers

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.pdpa.factory.PDPAFactory
import cn.sunline.saas.pdpa.modules.PDPAInformation
import cn.sunline.saas.pdpa.services.PDPAService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("pdpa")
class PDPAController {

    @Autowired
    private lateinit var pdpaFactory: PDPAFactory

    @Autowired
    private lateinit var pdpaService: PDPAService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

//    @GetMapping("{countryCode}/retrieve")
//    fun getPDPAInformation(@PathVariable countryCode: String): PDPAInformation? {
//        return pdpaFactory.getInstance(CountryType.valueOf(countryCode)).getPDPA()
//    }

    @GetMapping("{countryCode}/retrieve")
    fun getPDPAInformation(@PathVariable countryCode: String): ResponseEntity<DTOResponseSuccess<PDPAInformation>> {
        val pdpa = pdpaFactory.getInstance(CountryType.valueOf(countryCode)).getPDPA()
        return DTOResponseSuccess(pdpa).response()
    }

    @PostMapping(value = ["sign"], produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun sign(@RequestPart("customerId") customerId: String,@RequestPart("pdpaTemplateId") pdpaTemplateId: String, @RequestPart("signature") signature: MultipartFile):ResponseEntity<DTOResponseSuccess<String>> {
        val key = pdpaService.sign(customerId.toLong(),pdpaTemplateId.toLong(),signature.originalFilename!!,signature.inputStream)
        return DTOResponseSuccess(key).response()
    }
}