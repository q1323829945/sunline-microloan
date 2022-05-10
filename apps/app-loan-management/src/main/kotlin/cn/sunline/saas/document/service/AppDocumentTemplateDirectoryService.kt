package cn.sunline.saas.document.service

import cn.sunline.saas.document.dto.DTODocumentTemplateView
import cn.sunline.saas.document.dto.DTOTemplateDirectoryListView
import cn.sunline.saas.document.template.modules.DirectoryType
import cn.sunline.saas.document.template.modules.DocumentTemplateDirectory
import cn.sunline.saas.document.template.services.DocumentTemplateDirectoryService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AppDocumentTemplateDirectoryService {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @Autowired
    private lateinit var documentTemplateDirectoryService: DocumentTemplateDirectoryService


    fun getAll(): List<DTOTemplateDirectoryListView> {
        val directoryList = documentTemplateDirectoryService.queryAll()

        return getDirectoryTree(directoryList, null, null)
    }

    fun getDirectoryTree(directoryList:List<DocumentTemplateDirectory>, parentId: Long?, directoryPath:String?):List<DTOTemplateDirectoryListView>{
        val responseList = ArrayList<DTOTemplateDirectoryListView>()
        for(directory in directoryList){
            val dtoDirectoryList = ArrayList<DTOTemplateDirectoryListView>()
            //get directory children
            if(directory.parent?.id == parentId){
                val path = if(directoryPath == null){
                    directory.name
                }else {
                    "$directoryPath/${directory.name}"
                }

                dtoDirectoryList.addAll(getDirectoryTree(directoryList, directory.id, path))

                val dtoTemplateList = if (directory.templates.size > 0)
                    objectMapper.convertValue<List<DTODocumentTemplateView>>(directory.templates)
                else if(directory.loanUploadConfigures.size > 0)
                    objectMapper.convertValue(directory.loanUploadConfigures.filter { !it.deleted })
                else listOf()

                val dtoDirectory = DTOTemplateDirectoryListView(directory.id!!.toString(),directory.name,parentId,
                    directory.getTenantId()!!,dtoDirectoryList,dtoTemplateList,path,directory.directoryType)

                responseList.add(dtoDirectory)

            }

        }

        return responseList
    }
}