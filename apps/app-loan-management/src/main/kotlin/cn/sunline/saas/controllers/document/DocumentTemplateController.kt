package cn.sunline.saas.controllers.document

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
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.net.URLEncoder
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/DocumentTemplate")
class DocumentTemplateController {
    data class DTODocumentTemplateAdd(
            val name:String,
            var documentStoreReference:String?,
            val directoryId: Long,
            val languageType:LanguageType,
            val fileType:FileType,
            val documentType: DocumentType,
            val directoryPath:String,
    )

    data class DTODocumentTemplateView(
            val id:Long,
            val name:String,
            var documentStoreReference:String,
            val directoryId: Long,
            val languageType:LanguageType,
            val fileType:FileType,
            val directoryPath:String,
    )

    data class DTODocumentTemplateChange(
            val name:String,
            var documentStoreReference:String?,
            val directoryId: Long,
            val languageType:LanguageType,
            val fileType:FileType,
            val documentType: DocumentType,
            val directoryPath:String?,
    )

    @Autowired
    private lateinit var documentTemplateService: DocumentTemplateService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun getPaged(pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val paged = documentTemplateService.getPaged(pageable = pageable)

        return DTOPagedResponseSuccess(paged.map { objectMapper.convertValue<DTODocumentTemplateView>(it)}).response()
    }

    @PostMapping(produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun addOne(@RequestPart("template") documentTemplate: DTODocumentTemplateAdd,@RequestPart("file") file: MultipartFile): ResponseEntity<DTOResponseSuccess<DTODocumentTemplateView>> {
        documentTemplate.documentStoreReference = "${documentTemplate.directoryPath}/${file.originalFilename}"
        val template = objectMapper.convertValue<DocumentTemplate>(documentTemplate)
        val saveTemplate = documentTemplateService.addDocumentTemplate(template,file.inputStream)
        val responseTemplate = objectMapper.convertValue<DTODocumentTemplateView>(saveTemplate)

        return DTOResponseSuccess(responseTemplate).response()
    }

    @PostMapping(value = ["{id}"],produces = [MediaType.APPLICATION_JSON_VALUE], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun updateOne(@PathVariable id: Long,@RequestPart("template") dtoTemplate: DTODocumentTemplateChange,@RequestPart("file") file: MultipartFile?): ResponseEntity<DTOResponseSuccess<DTODocumentTemplateView>>{

        val oldOne = documentTemplateService.getOne(id)?:throw Exception("Invalid template")

        file?.originalFilename?.run {
            dtoTemplate.documentStoreReference = "${dtoTemplate.directoryPath}/${file.originalFilename}"
        }

        val newOne = objectMapper.convertValue<DocumentTemplate>(dtoTemplate)
        val updateTemplate = documentTemplateService.updateDocumentTemplate(oldOne,newOne,file?.inputStream)
        val responseTemplate = objectMapper.convertValue<DTODocumentTemplateView>(updateTemplate)
        return DTOResponseSuccess(responseTemplate).response()
    }

    @DeleteMapping("{id}")
    fun deleteOne(@PathVariable id: Long): ResponseEntity<DTOResponseSuccess<DTODocumentTemplateView>> {
        val documentTemplate = documentTemplateService.getOne(id)?: throw Exception("Invalid template")
        documentTemplateService.delete(documentTemplate)
        val responseDocumentTemplate = objectMapper.convertValue<DTODocumentTemplateView>(documentTemplate)
        return DTOResponseSuccess(responseDocumentTemplate).response()
    }



    @GetMapping("download/{id}")
    fun download(@PathVariable id:Long,response: HttpServletResponse) {
        val template = documentTemplateService.getOne(id)?:throw Exception("Invalid template")
        val inputStream = documentTemplateService.download(template)

        val fileName = if(template.documentStoreReference.lastIndexOf("/") == -1){
            template.documentStoreReference
        }else{
            template.documentStoreReference.substring(template.documentStoreReference.lastIndexOf("/"))
        }

        response.reset();
        response.contentType = "application/octet-stream";
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        val outputStream = response.outputStream

        val bytes = ByteArray(1024)
        var len = 0
        while (true){
            len = inputStream.read(bytes)

            if(len == -1){
                break
            }
            outputStream.write(bytes,0,len)
        }

        inputStream.close()
    }
}
