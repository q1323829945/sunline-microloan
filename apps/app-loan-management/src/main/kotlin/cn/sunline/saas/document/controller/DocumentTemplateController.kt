package cn.sunline.saas.document.controller

import cn.sunline.saas.document.controller.dto.DTODocumentTemplateAdd
import cn.sunline.saas.document.controller.dto.DTODocumentTemplateChange
import cn.sunline.saas.document.controller.dto.DTODocumentTemplateView
import cn.sunline.saas.document.exception.DocumentTemplateNotFoundException
import cn.sunline.saas.document.service.AppDocumentTemplateService
import cn.sunline.saas.document.template.modules.db.DocumentTemplate
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.document.template.services.DocumentTemplateService
import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
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
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/DocumentTemplate")
class DocumentTemplateController {

    @Autowired
    private lateinit var documentTemplateService: DocumentTemplateService

    @Autowired
    private lateinit var appDocumentTemplateService: AppDocumentTemplateService


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun getPaged(pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = documentTemplateService.getPaged(pageable = pageable)
        return DTOPagedResponseSuccess(paged.map { objectMapper.convertValue<DTODocumentTemplateView>(it)}).response()
    }


    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun addOne(@RequestPart("template") documentTemplate: DTODocumentTemplateAdd, @RequestPart("file") file: MultipartFile): ResponseEntity<DTOResponseSuccess<DTODocumentTemplateView>> {
        documentTemplate.documentStoreReference = "${documentTemplate.directoryPath}/${file.originalFilename}"
        documentTemplate.fileType = getFileType(file.originalFilename!!)
        val template = objectMapper.convertValue<DocumentTemplate>(documentTemplate)
        val saveTemplate = documentTemplateService.addDocumentTemplate(template,file.inputStream)
        val responseTemplate = objectMapper.convertValue<DTODocumentTemplateView>(saveTemplate)

        return DTOResponseSuccess(responseTemplate).response()
    }

    @PostMapping(value = ["{id}"],produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateOne(@PathVariable id: Long, @RequestPart("template") dtoTemplate: DTODocumentTemplateChange, @RequestPart("file") file: MultipartFile?): ResponseEntity<DTOResponseSuccess<DTODocumentTemplateView>>{

        val oldOne = documentTemplateService.getOne(id)?:throw DocumentTemplateNotFoundException("Invalid template")
        file?.originalFilename?.run {
            dtoTemplate.documentStoreReference = "${dtoTemplate.directoryPath}/${file.originalFilename}"
            dtoTemplate.fileType = getFileType(file.originalFilename!!)
        }
        val newOne = objectMapper.convertValue<DocumentTemplate>(dtoTemplate)
        val updateTemplate = documentTemplateService.updateDocumentTemplate(oldOne,newOne,file?.inputStream)
        val responseTemplate = objectMapper.convertValue<DTODocumentTemplateView>(updateTemplate)
        return DTOResponseSuccess(responseTemplate).response()
    }

    @DeleteMapping("{id}")
    fun deleteOne(@PathVariable id: Long): ResponseEntity<DTOResponseSuccess<DTODocumentTemplateView>> {
        val documentTemplate = documentTemplateService.getOne(id)?: throw ManagementException(ManagementExceptionCode.DATA_NOT_FOUND,"Invalid template")
        documentTemplateService.delete(documentTemplate)
        val responseDocumentTemplate = objectMapper.convertValue<DTODocumentTemplateView>(documentTemplate)
        return DTOResponseSuccess(responseDocumentTemplate).response()
    }

    @GetMapping("download/{id}")
    fun download(@PathVariable id:Long,response: HttpServletResponse) {
        appDocumentTemplateService.download(id, response)
    }


    private fun getFileType(originalFilename:String):FileType{
        try{
            return FileType.valueOf(originalFilename.substring(originalFilename.lastIndexOf(".")+1).uppercase())
        } catch (e:Exception){
            throw ManagementException(ManagementExceptionCode.TYPE_ERROR,"file type error")
        }
    }
}