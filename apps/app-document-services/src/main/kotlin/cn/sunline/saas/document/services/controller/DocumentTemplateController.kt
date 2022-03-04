package cn.sunline.saas.document.services.controller

import cn.sunline.saas.document.model.DocumentType
import cn.sunline.saas.document.template.modules.DocumentTemplate
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.document.template.modules.LanguageType
import cn.sunline.saas.document.template.services.DocumentTemplateService
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/document-templates")
class DocumentTemplateController {
    data class DTODocumentTemplateAdd(
            val name:String,
            val documentStoreReference:String,
            val bucketName:String,
            val languageType:LanguageType,
            val fileType:FileType,
            val documentType: DocumentType
    )

    data class DTODocumentTemplateView(
            val id:Long,
            val name:String,
            val documentStoreReference:String,
            val bucketName:String,
            val languageType:LanguageType,
            val fileType:FileType,
            val documentType:DocumentType
    )


    @Autowired
    private lateinit var documentTemplateService: DocumentTemplateService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun getPaged(pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = documentTemplateService.getPaged(pageable = pageable)

        return DTOPagedResponseSuccess(paged.map { objectMapper.convertValue<DTODocumentTemplateView>(it)}).response()
    }

    @PostMapping
    fun addOne(@RequestBody documentTemplate:DTODocumentTemplateAdd): ResponseEntity<DTOResponseSuccess<DTODocumentTemplateView>> {
        println(documentTemplate)
        val template = objectMapper.convertValue<DocumentTemplate>(documentTemplate)
        val saveTemplate = documentTemplateService.save(template)
        val responseTemplate = objectMapper.convertValue<DTODocumentTemplateView>(saveTemplate)
        return DTOResponseSuccess(responseTemplate).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long,@RequestBody dtoTemplate:DTODocumentTemplateAdd): ResponseEntity<DTOResponseSuccess<DTODocumentTemplateView>>{
        val oldOne = documentTemplateService.getOne(id)?:throw Exception("Invalid template")
        val newOne = objectMapper.convertValue<DocumentTemplate>(dtoTemplate)
        val updateTemplate = documentTemplateService.updateDocumentTemplate(oldOne,newOne)
        val responseTemplate = objectMapper.convertValue<DTODocumentTemplateView>(updateTemplate)
        return DTOResponseSuccess(responseTemplate).response()
    }

}