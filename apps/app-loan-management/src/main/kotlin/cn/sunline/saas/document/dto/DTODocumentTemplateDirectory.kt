package cn.sunline.saas.document.dto

import cn.sunline.saas.document.template.modules.DocumentTemplateDirectory
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.document.template.modules.LanguageType

data class DTOTemplateDirectoryAdd(
    val name:String,
    val parentId: Long?,
    val tenantId:Long,
    var parent: DocumentTemplateDirectory?
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
