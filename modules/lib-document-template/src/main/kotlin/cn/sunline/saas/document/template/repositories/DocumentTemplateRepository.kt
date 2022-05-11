package cn.sunline.saas.document.template.repositories

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.document.template.modules.db.DocumentTemplate

interface DocumentTemplateRepository: BaseRepository<DocumentTemplate,Long> {
    fun findByDocumentStoreReference(documentStoreReference:String): DocumentTemplate?
}