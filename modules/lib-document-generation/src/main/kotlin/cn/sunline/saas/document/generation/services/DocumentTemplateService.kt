package cn.sunline.saas.document.generation.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.document.generation.models.DocumentTemplate
import cn.sunline.saas.document.generation.repositories.DocumentTemplateRepository
import org.springframework.stereotype.Service

@Service
class DocumentTemplateService(private val documentTemplateRepository: DocumentTemplateRepository):
    BaseRepoService<DocumentTemplate,Long>(documentTemplateRepository){

}