package cn.sunline.saas.document.controller.dto

import cn.sunline.saas.document.template.modules.DirectoryType
import cn.sunline.saas.document.template.modules.db.DocumentTemplateDirectory

data class DTOTemplateDirectoryAdd(
    val name:String,
    val parentId: Long?,
    var parent: DocumentTemplateDirectory?,
    val directoryType: DirectoryType,
)

data class DTOTemplateDirectoryListView(
    val id:String,
    val name:String,
    val parentId: Long?,
    val directory: List<DTOTemplateDirectoryListView>?,
    val templates:List<DTODocumentTemplateView>?,
    val directoryPath:String?,
    val directoryType: DirectoryType
)

data class DTOTemplateDirectoryView(
    val id:String,
    val name:String,
    val parentId: Long?,
    val parent: DTOTemplateDirectoryView?,
    val templates:List<DTODocumentTemplateView>?,
    val directoryType: DirectoryType
)

data class DTOTemplateDirectoryChange(
    val id:Long,
    val name:String,
)
