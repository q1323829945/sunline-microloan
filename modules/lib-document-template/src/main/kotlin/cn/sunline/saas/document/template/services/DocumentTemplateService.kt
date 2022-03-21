package cn.sunline.saas.document.template.services

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.document.template.modules.DocumentTemplate
import cn.sunline.saas.document.template.repositories.DocumentTemplateRepository
import cn.sunline.saas.huaweicloud.config.HuaweiCloudTools
import cn.sunline.saas.obs.api.DeleteParams
import cn.sunline.saas.obs.api.GetParams
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class DocumentTemplateService(private val documentTemplateRepository: DocumentTemplateRepository):
        BaseRepoService<DocumentTemplate,Long>(documentTemplateRepository){

    @Autowired
    private lateinit var huaweiCloudService: ObsApi

    @Autowired
    private lateinit var huaweiCloudTools:HuaweiCloudTools

    fun addDocumentTemplate(documentTemplate: DocumentTemplate,inputStream: InputStream):DocumentTemplate{

        val putParams = PutParams(huaweiCloudTools.bucketName,documentTemplate.documentStoreReference,inputStream)

        huaweiCloudService.putObject(putParams)

        return this.save(documentTemplate)
    }

    fun updateDocumentTemplate(oldOne:DocumentTemplate,newOne:DocumentTemplate,inputStream: InputStream?):DocumentTemplate{
        inputStream?.run {
            val deleteParams = DeleteParams(huaweiCloudTools.bucketName,oldOne.documentStoreReference)
            huaweiCloudService.deleteObject(deleteParams)

            val putParams = PutParams(huaweiCloudTools.bucketName,newOne.documentStoreReference,inputStream)
            huaweiCloudService.putObject(putParams)

            oldOne.documentStoreReference = newOne.documentStoreReference
        }
        oldOne.name = newOne.name
        oldOne.languageType = newOne.languageType
        oldOne.documentType = newOne.documentType
        oldOne.fileType = newOne.fileType
        return this.save(oldOne)
    }

    fun delete(documentTemplate:DocumentTemplate){
        val deleteParams = DeleteParams(huaweiCloudTools.bucketName,documentTemplate.documentStoreReference)
        huaweiCloudService.deleteObject(deleteParams)


        documentTemplateRepository.deleteById(documentTemplate.id!!)
    }

    fun download(documentTemplate: DocumentTemplate): InputStream {
        val getParams = GetParams(huaweiCloudTools.bucketName, documentTemplate.documentStoreReference)

        return huaweiCloudService.getObject(getParams) as InputStream
    }
}