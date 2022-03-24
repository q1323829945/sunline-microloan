package cn.sunline.saas.controllers

import cn.sunline.saas.global.constant.CountryType
import cn.sunline.saas.pdpa.factory.PDPAFactory
import cn.sunline.saas.pdpa.modules.db.PDPA
import cn.sunline.saas.pdpa.modules.dto.PDPAInformationView
import cn.sunline.saas.pdpa.modules.dto.PDPAView
import cn.sunline.saas.pdpa.services.PDPAService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("pdpa")
class PDPAController {

    data class PDPAInfo(
            val pdpaTemplateId:Long
    )

    @Autowired
    private lateinit var pdpaFactory: PDPAFactory

    @Autowired
    private lateinit var pdpaService: PDPAService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @GetMapping("{countryCode}/retrieve")
    fun getPDPAInformation(@PathVariable countryCode: String): ResponseEntity<DTOResponseSuccess<PDPAInformationView>> {

        val pdpa = pdpaFactory.getInstance(CountryType.valueOf(countryCode)).getPDPA()

        return DTOResponseSuccess(pdpa).response()
    }

    @PostMapping(value = ["sign"], produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun signature(@RequestPart("pdpaTemplateId") pdpaTemplateId: Long, @RequestPart("signature") signature: MultipartFile) {
        pdpaService.sign(pdpaTemplateId,signature.originalFilename!!,signature.inputStream)
    }


    @PostMapping
    fun addPDPA(@RequestBody pdpaAdd: PDPAInfo): ResponseEntity<DTOResponseSuccess<PDPAView>> {
        val pdpa = objectMapper.convertValue<PDPA>(pdpaAdd)

        val save = pdpaService.addPDPA(pdpa)

        val responseEntity = objectMapper.convertValue<PDPAView>(save)

        return DTOResponseSuccess(responseEntity).response()
    }

    @GetMapping("{customerId}")
    fun getPDPA(@PathVariable customerId:Long): ResponseEntity<DTOResponseSuccess<PDPAView>> {
        val pdpa = pdpaService.getPDPAByCustomerId(customerId)

        val responseEntity = objectMapper.convertValue<PDPAView>(pdpa)

        return DTOResponseSuccess(responseEntity).response()
    }



}