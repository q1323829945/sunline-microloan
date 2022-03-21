package cn.sunline.saas.document.template.services

import cn.sunline.saas.document.template.modules.DocumentTemplateDirectory
import cn.sunline.saas.document.template.repositories.DocumentTemplateDirectoryRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence

/**
 * @title: DocumentDirectoryService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/21 16:27
 */
@Service
class DocumentTemplateDirectoryService(private val documentTemplateDirectoryRepo: DocumentTemplateDirectoryRepository,private val sequence: Sequence) :
    BaseMultiTenantRepoService<DocumentTemplateDirectory, Long>(documentTemplateDirectoryRepo){

    fun queryAll():List<DocumentTemplateDirectory>{
        return documentTemplateDirectoryRepo.queryAll()
    }

    fun addOne(documentTemplateDirectory: DocumentTemplateDirectory):DocumentTemplateDirectory{
        val id = sequence.nextId()
        documentTemplateDirectory.id = id
        return this.save(documentTemplateDirectory)
    }

    fun update(oldOne: DocumentTemplateDirectory, newOne: DocumentTemplateDirectory): DocumentTemplateDirectory {
        oldOne.name = newOne.name
        oldOne.version = (oldOne.version.toLong() + 1L).toString()
        return this.save(oldOne)
    }

    fun delete(directory: DocumentTemplateDirectory):DocumentTemplateDirectory{
        directory.deleted = true
        return this.save(directory)
    }

}