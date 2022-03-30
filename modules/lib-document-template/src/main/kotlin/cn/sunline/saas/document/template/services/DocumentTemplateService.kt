package cn.sunline.saas.document.template.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.document.template.modules.DocumentTemplate
import cn.sunline.saas.document.template.repositories.DocumentTemplateRepository
import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.obs.api.DeleteParams
import cn.sunline.saas.obs.api.GetParams
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class DocumentTemplateService(private val documentTemplateRepository: DocumentTemplateRepository) :
    BaseRepoService<DocumentTemplate, Long>(documentTemplateRepository) {

    @Autowired
    private lateinit var obs: ObsApi

    fun addDocumentTemplate(documentTemplate: DocumentTemplate, inputStream: InputStream): DocumentTemplate {
        val checkTemplate =
            documentTemplateRepository.findByDocumentStoreReference(documentTemplate.documentStoreReference)
        if (checkTemplate != null) {
            throw ManagementException(ManagementExceptionCode.DATA_ALREADY_EXIST, "document already exist")
        }

        val putParams = PutParams(documentTemplate.documentStoreReference, inputStream)

        obs.putObject(putParams)

        return this.save(documentTemplate)
    }


    fun updateDocumentTemplate(
        oldOne: DocumentTemplate,
        newOne: DocumentTemplate,
        inputStream: InputStream?
    ): DocumentTemplate {
        inputStream?.run {
            val checkTemplate = documentTemplateRepository.findByDocumentStoreReference(newOne.documentStoreReference)
            if (checkTemplate != null && oldOne.documentStoreReference != newOne.documentStoreReference) {
                throw ManagementException(ManagementExceptionCode.DATA_ALREADY_EXIST, "document already exist")
            }

            val deleteParams = DeleteParams(oldOne.documentStoreReference)
            obs.deleteObject(deleteParams)

            val putParams = PutParams(newOne.documentStoreReference, inputStream)
            obs.putObject(putParams)

            oldOne.documentStoreReference = newOne.documentStoreReference
        }
        oldOne.name = newOne.name
        oldOne.languageType = newOne.languageType
        oldOne.documentType = newOne.documentType
        oldOne.fileType = newOne.fileType
        return this.save(oldOne)
    }

    fun delete(documentTemplate: DocumentTemplate) {
        val deleteParams = DeleteParams(documentTemplate.documentStoreReference)
        obs.deleteObject(deleteParams)


        documentTemplateRepository.deleteById(documentTemplate.id!!)
    }

    fun download(documentTemplate: DocumentTemplate): InputStream {
        val getParams = GetParams(documentTemplate.documentStoreReference)

        return obs.getObject(getParams) as InputStream
    }
}