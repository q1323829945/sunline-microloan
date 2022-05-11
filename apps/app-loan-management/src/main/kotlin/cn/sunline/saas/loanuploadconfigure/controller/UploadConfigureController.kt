package cn.sunline.saas.loanuploadconfigure.controller

import cn.sunline.saas.document.template.services.LoanUploadConfigureService
import cn.sunline.saas.loanuploadconfigure.controller.dto.DTOUploadConfigure
import cn.sunline.saas.loanuploadconfigure.service.UploadConfigureService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/LoanUploadConfigure")
class UploadConfigureController {

    @Autowired
    private lateinit var loanUploadConfigureService: LoanUploadConfigureService

    @Autowired
    private lateinit var appLoanUploadConfigureService: UploadConfigureService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun getPaged(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>{
        val paged = appLoanUploadConfigureService.getPaged(pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @GetMapping("all")
    fun getAll(pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>{
        val paged = appLoanUploadConfigureService.getPaged(pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @PostMapping
    fun addUploadConfigure(@RequestBody dtoUploadConfigureAdd: DTOUploadConfigure): ResponseEntity<DTOResponseSuccess<DTOUploadConfigure>>{
        val result = appLoanUploadConfigureService.addUploadConfigure(dtoUploadConfigureAdd)
        return DTOResponseSuccess(result).response()
    }

    @DeleteMapping("{id}")
    fun deleteUploadConfigure(@PathVariable id:Long): ResponseEntity<DTOResponseSuccess<DTOUploadConfigure>>{
        val result = appLoanUploadConfigureService.deleteUploadConfigure(id)
        val responseEntity = objectMapper.convertValue<DTOUploadConfigure>(result)
        return DTOResponseSuccess(responseEntity).response()
    }
}