package cn.sunline.saas.document.controller

import cn.sunline.saas.document.controller.dto.DTOTemplateDirectoryAdd
import cn.sunline.saas.document.controller.dto.DTOTemplateDirectoryChange
import cn.sunline.saas.document.controller.dto.DTOTemplateDirectoryListView
import cn.sunline.saas.document.controller.dto.DTOTemplateDirectoryView
import cn.sunline.saas.document.exception.DocumentDirectoryNotFoundException
import cn.sunline.saas.document.service.AppDocumentTemplateDirectoryService
import cn.sunline.saas.document.template.modules.db.DocumentTemplateDirectory
import cn.sunline.saas.document.template.services.DocumentTemplateDirectoryService
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/DocumentTemplateDirectory")
class DocumentTemplateDirectoryController {


    @Autowired
    private lateinit var documentTemplateDirectoryService: DocumentTemplateDirectoryService

    @Autowired
    private lateinit var appDocumentTemplateDirectoryService: AppDocumentTemplateDirectoryService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @GetMapping
    fun getAll():  ResponseEntity<DTOResponseSuccess<List<DTOTemplateDirectoryListView>>> {
        val list = appDocumentTemplateDirectoryService.getAll()
        return DTOResponseSuccess(list).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoDocumentDirectory: DTOTemplateDirectoryAdd): ResponseEntity<DTOResponseSuccess<DTOTemplateDirectoryView>> {
        dtoDocumentDirectory.parentId?.run {
            val parent = documentTemplateDirectoryService.getOne(this)?:throw DocumentDirectoryNotFoundException("parent directory is invalid",
                ManagementExceptionCode.DATA_NOT_FOUND)
            dtoDocumentDirectory.parent = parent
        }
        val documentDirectory = objectMapper.convertValue<DocumentTemplateDirectory>(dtoDocumentDirectory)
        val saveDirectory = documentTemplateDirectoryService.addOne(documentDirectory)
        val responseDirectory = objectMapper.convertValue<DTOTemplateDirectoryView>(saveDirectory)
        return DTOResponseSuccess(responseDirectory).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoDirectory: DTOTemplateDirectoryChange): ResponseEntity<DTOResponseSuccess<DTOTemplateDirectoryView>> {
        val oldOne = documentTemplateDirectoryService.getOne(id)?:throw DocumentDirectoryNotFoundException("Invalid directory",ManagementExceptionCode.DATA_NOT_FOUND)
        val newOne = objectMapper.convertValue<DocumentTemplateDirectory>(dtoDirectory)
        val updateOne = documentTemplateDirectoryService.update(oldOne,newOne)
        val responseDirectory = objectMapper.convertValue<DTOTemplateDirectoryView>(updateOne)
        return DTOResponseSuccess(responseDirectory).response()
    }

    @DeleteMapping("{id}")
    fun deleteOne(@PathVariable id: Long): ResponseEntity<DTOResponseSuccess<DTOTemplateDirectoryView>> {
        val directory = documentTemplateDirectoryService.getOne(id)?:throw DocumentDirectoryNotFoundException("Invalid directory",ManagementExceptionCode.DATA_NOT_FOUND)
        val delete = documentTemplateDirectoryService.delete(directory)
        val responseDirectory = objectMapper.convertValue<DTOTemplateDirectoryView>(delete)
        return DTOResponseSuccess(responseDirectory).response()
    }



}