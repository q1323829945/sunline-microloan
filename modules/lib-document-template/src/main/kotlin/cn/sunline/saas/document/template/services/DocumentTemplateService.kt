package cn.sunline.saas.document.template.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.document.template.modules.DocumentTemplate
import cn.sunline.saas.document.template.repositories.DocumentTemplateRepository
import org.springframework.stereotype.Service

@Service
class DocumentTemplateService(private val documentTemplateRepository: DocumentTemplateRepository):
    BaseRepoService<DocumentTemplate,Long>(documentTemplateRepository){

    fun updateDocumentTemplate(oldOne:DocumentTemplate,newOne:DocumentTemplate):DocumentTemplate{
        oldOne.bucketName = newOne.bucketName
        oldOne.name = newOne.name
        oldOne.documentStoreReference = newOne.documentStoreReference
        oldOne.languageType = newOne.languageType
        oldOne.documentType = newOne.documentType

        return this.save(oldOne)
    }
}