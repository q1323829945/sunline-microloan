package cn.sunline.saas.document.dto

import cn.sunline.saas.document.model.DocumentType
import cn.sunline.saas.document.template.modules.FileType
import cn.sunline.saas.document.template.modules.LanguageType

data class DTODocumentTemplateAdd(
    val name:String,
    var documentStoreReference:String?,
    val directoryId: Long,
    val languageType: LanguageType,
    var fileType: FileType?,
    val documentType: DocumentType,
    val directoryPath:String,
)

data class DTODocumentTemplateView(
    val id:String,
    val name:String,
    var documentStoreReference:String,
    val directoryId: Long,
    val languageType: LanguageType,
    val fileType: FileType
)

data class DTODocumentTemplateChange(
    val name:String,
    var documentStoreReference:String?,
    val directoryId: Long,
    val languageType: LanguageType,
    var fileType: FileType,
    val documentType: DocumentType,
    val directoryPath:String?,
)

