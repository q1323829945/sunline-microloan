package cn.sunline.saas.document.dto

import cn.sunline.saas.document.template.modules.DirectoryType
import cn.sunline.saas.document.template.modules.DocumentTemplateDirectory
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.document.template.modules.LanguageType
import cn.sunline.saas.loan.configure.modules.db.LoanUploadConfigure

data class DTOTemplateDirectoryAdd(
    val name:String,
    val parentId: Long?,
    val tenantId:Long,
    var parent: DocumentTemplateDirectory?,
    val directoryType: DirectoryType,
)

data class DTOTemplateDirectoryListView(
    val id:String,
    val name:String,
    val parentId: Long?,
    val tenantId:Long,
    val directory: List<DTOTemplateDirectoryListView>?,
    val templates:List<DTODocumentTemplateView>?,
    val directoryPath:String?,
    val directoryType: DirectoryType
//    val loanUploadConfigure: List<DTODocumentTemplateView>?
)

data class DTOTemplateDirectoryView(
    val id:String,
    val name:String,
    val parentId: Long?,
    val tenantId:Long,
    val parent: DTOTemplateDirectoryView?,
    val templates:List<DTODocumentTemplateView>?,
    val directoryType: DirectoryType
//    val loanUploadConfigure: List<DTODocumentTemplateView>?,
)

data class DTOTemplateDirectoryChange(
    val id:Long,
    val name:String,
    val tenantId:Long
)
