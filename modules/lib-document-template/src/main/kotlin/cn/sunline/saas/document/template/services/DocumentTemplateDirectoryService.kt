package cn.sunline.saas.document.template.services

import cn.sunline.saas.document.template.modules.db.DocumentTemplateDirectory
import cn.sunline.saas.document.template.repositories.DocumentTemplateDirectoryRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import org.springframework.data.domain.Pageable
import javax.persistence.criteria.Predicate

@Service
class DocumentTemplateDirectoryService(private val documentTemplateDirectoryRepo: DocumentTemplateDirectoryRepository,private val sequence: Sequence) :
    BaseMultiTenantRepoService<DocumentTemplateDirectory, Long>(documentTemplateDirectoryRepo){

    fun queryAll(): List<DocumentTemplateDirectory> {

        val page = getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Boolean>("deleted"), false))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },Pageable.unpaged())

        return page.toList()

    }

    fun addOne(documentTemplateDirectory: DocumentTemplateDirectory): DocumentTemplateDirectory {
        val id = sequence.nextId()
        documentTemplateDirectory.id = id
        return this.save(documentTemplateDirectory)
    }

    fun update(oldOne: DocumentTemplateDirectory, newOne: DocumentTemplateDirectory): DocumentTemplateDirectory {
        oldOne.name = newOne.name
        oldOne.version = (oldOne.version.toLong() + 1L).toString()
        return this.save(oldOne)
    }

    fun delete(directory: DocumentTemplateDirectory): DocumentTemplateDirectory {
        directory.deleted = true
        return this.save(directory)
    }

}