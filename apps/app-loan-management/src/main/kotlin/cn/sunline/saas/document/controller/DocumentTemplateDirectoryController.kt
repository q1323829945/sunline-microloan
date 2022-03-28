package cn.sunline.saas.document.controller

import cn.sunline.saas.document.exception.DocumentDirectoryNotFoundException
import cn.sunline.saas.document.template.modules.DocumentTemplateDirectory
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.document.template.modules.LanguageType
import cn.sunline.saas.document.template.services.DocumentTemplateDirectoryService
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
    data class DTOTemplateDirectoryAdd(
            val name:String,
            val parentId: Long?,
            val tenantId:Long,
            var parent:DocumentTemplateDirectory?
    )

    data class DTOTemplateDirectoryListView(
        val id:Long,
        val name:String,
        val parentId: Long?,
        val tenantId:Long,
        val directory: List<DTOTemplateDirectoryListView>,
        val templates:List<DTODocumentTemplateView>,
        val directoryPath:String
    )

    data class DTODocumentTemplateView(
            val id:Long,
            val name:String,
            var documentStoreReference:String,
            val directoryId: Long,
            val languageType: LanguageType,
            val fileType: FileType
    )

    data class DTOTemplateDirectoryView(
        val id:Long,
        val name:String,
        val parentId: Long?,
        val tenantId:Long,
        val parent: DTOTemplateDirectoryView?,
        val templates:List<DTODocumentTemplateView>
    )

    data class DTOTemplateDirectoryChange(
            val id:Long,
            val name:String,
            val tenantId:Long
    )

    @Autowired
    private lateinit var documentTemplateDirectoryService: DocumentTemplateDirectoryService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @GetMapping
    fun getAll():  ResponseEntity<DTOResponseSuccess<List<DTOTemplateDirectoryListView>>> {
        //TODO:
        val directoryList = documentTemplateDirectoryService.queryAll()

        val responseDirectory = getDirectoryTree(directoryList,null,null)

        return DTOResponseSuccess(responseDirectory).response()
    }

    fun getDirectoryTree(directoryList:List<DocumentTemplateDirectory>,parentId: Long?,directoryPath:String?):List<DTOTemplateDirectoryListView>{
        val responseList = ArrayList<DTOTemplateDirectoryListView>()
        for(directory in directoryList){
            val dtoDirectoryList = ArrayList<DTOTemplateDirectoryListView>()
            //get directory children
            if(directory.parent?.id == parentId){
                val path = if(directoryPath == null){
                    directory.name
                }else{
                    "$directoryPath/${directory.name}"
                }

                dtoDirectoryList.addAll(getDirectoryTree(directoryList, directory.id,path))
                val dtoTemplateList = objectMapper.convertValue<List<DTODocumentTemplateView>>(directory.templates)

                val dtoDirectory = DTOTemplateDirectoryListView(directory.id!!,directory.name,parentId,directory.getTenantId()!!,dtoDirectoryList,dtoTemplateList,path)
                responseList.add(dtoDirectory)
            }

        }

        return responseList
    }

    @PostMapping
    fun addOne(@RequestBody dtoDocumentDirectory: DTOTemplateDirectoryAdd): ResponseEntity<DTOResponseSuccess<DTOTemplateDirectoryView>> {
        dtoDocumentDirectory.parentId?.run {
            dtoDocumentDirectory.parent = parent
        }
        val documentDirectory = objectMapper.convertValue<DocumentTemplateDirectory>(dtoDocumentDirectory)
        val saveDirectory = documentTemplateDirectoryService.addOne(documentDirectory)
        val responseDirectory = objectMapper.convertValue<DTOTemplateDirectoryView>(saveDirectory)
        return DTOResponseSuccess(responseDirectory).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: Long, @RequestBody dtoDirectory: DTOTemplateDirectoryChange): ResponseEntity<DTOResponseSuccess<DTOTemplateDirectoryView>> {
        val oldOne = documentTemplateDirectoryService.getOne(id)?:throw NotFoundException("Invalid directory")        val newOne = objectMapper.convertValue<DocumentTemplateDirectory>(dtoDirectory)
        val updateOne = documentTemplateDirectoryService.update(oldOne,newOne)
        val responseDirectory = objectMapper.convertValue<DTOTemplateDirectoryView>(updateOne)
        return DTOResponseSuccess(responseDirectory).response()
    }

    @DeleteMapping("{id}")
    fun deleteOne(@PathVariable id: Long): ResponseEntity<DTOResponseSuccess<DTOTemplateDirectoryView>> {
        val directory = documentTemplateDirectoryService.getOne(id)?:throw NotFoundException("Invalid directory")        val delete = documentTemplateDirectoryService.delete(directory)
        val responseDirectory = objectMapper.convertValue<DTOTemplateDirectoryView>(delete)
        return DTOResponseSuccess(responseDirectory).response()
    }



}