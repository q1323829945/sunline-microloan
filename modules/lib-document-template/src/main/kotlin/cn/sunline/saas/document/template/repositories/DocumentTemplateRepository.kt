package cn.sunline.saas.document.template.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.document.template.modules.DocumentTemplate
import org.springframework.data.jpa.repository.Query

interface DocumentTemplateRepository: BaseRepository<DocumentTemplate,Long> {
    fun findByDocumentStoreReference(documentStoreReference:String):DocumentTemplate?
}