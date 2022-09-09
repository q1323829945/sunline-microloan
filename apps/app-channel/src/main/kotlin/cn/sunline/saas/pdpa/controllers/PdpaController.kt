package cn.sunline.saas.pdpa.controllers

import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.pdpa.exception.PdpaNotFoundException
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAdd
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaChange
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaView
import cn.sunline.saas.pdpa.services.PdpaService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.websocket.server.PathParam


@RestController
@RequestMapping("pdpa")
class PdpaController {
    @Autowired
    private lateinit var pdpaService: PdpaService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping("{country}/{language}/retrieve")
    fun getPDPAInformation(@PathVariable country: CountryType,@PathVariable language:LanguageType): ResponseEntity<DTOResponseSuccess<DTOPdpaView>> {
        val pdpa = pdpaService.getByCountryAndLanguage(country,language)?:throw PdpaNotFoundException("Invalid pdpa")
        val dtoPdpa = pdpaService.getDTOPdpaView(pdpa.id)
        return DTOResponseSuccess(dtoPdpa).response()
    }

    @PostMapping(value = ["sign"], produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun sign(@RequestPart("customerId") customerId: String,@RequestPart("pdpaTemplateId") pdpaTemplateId: String, @RequestPart("signature") signature: MultipartFile):ResponseEntity<DTOResponseSuccess<String>> {
        val key = pdpaService.sign(customerId.toLong(),pdpaTemplateId.toLong(),signature.originalFilename!!,signature.inputStream)
        return DTOResponseSuccess(key).response()
    }

    @GetMapping
    fun paged(@PathParam("country")country: CountryType?,pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val pdpa = pdpaService.getPdpaPaged(country,pageable)
        return DTOPagedResponseSuccess(pdpa.map { objectMapper.convertValue<DTOPdpaView>(it) }).response()
    }

    @GetMapping("{id}")
    fun getOne(@PathVariable id:String):ResponseEntity<DTOResponseSuccess<DTOPdpaView>>{
        val pdpa = pdpaService.getDTOPdpaView(id.toLong())
        return DTOResponseSuccess(pdpa).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtopdpaAdd: DTOPdpaAdd):ResponseEntity<DTOResponseSuccess<DTOPdpaView>>{
        val pdpa = pdpaService.addOne(dtopdpaAdd)
        return DTOResponseSuccess(pdpa).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id:String,@RequestBody dtoPdpaChange: DTOPdpaChange):ResponseEntity<DTOResponseSuccess<DTOPdpaView>>{
        val pdpa = pdpaService.updateOne(id.toLong(),dtoPdpaChange)
        return DTOResponseSuccess(pdpa).response()
    }
}