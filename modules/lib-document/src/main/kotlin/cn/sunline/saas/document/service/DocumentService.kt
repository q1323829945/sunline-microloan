package cn.sunline.saas.document.service

import cn.sunline.saas.document.model.Document
import cn.sunline.saas.document.repository.DocumentRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service

/**
 * @title: DocumentService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/21 16:24
 */
@Service
class DocumentService(private val documentRepo: DocumentRepository): BaseMultiTenantRepoService<Document, Long>(documentRepo){

    fun retrieveByDirectory(directoryId:Long):MutableList<Document>?{
        return documentRepo.findByDirectoryId(directoryId)
    }


}