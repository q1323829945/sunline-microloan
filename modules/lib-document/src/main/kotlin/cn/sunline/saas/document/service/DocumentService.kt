package cn.sunline.saas.document.service

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.document.model.Document
import cn.sunline.saas.document.model.DocumentStatus
import cn.sunline.saas.document.repository.DocumentRepository
import cn.sunline.saas.huaweicloud.config.BUCKET_NAME
import cn.sunline.saas.huaweicloud.config.REGION
import cn.sunline.saas.huaweicloud.model.HuaweiCloudData
import cn.sunline.saas.huaweicloud.services.HuaweiCloudService
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

/**
 * @title: DocumentService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/2/21 16:24
 */
@Service
class DocumentService(private val documentRepo:DocumentRepository):BaseRepoService<Document,Long>(documentRepo){
    @Autowired
    private lateinit var huaweiCloudService: ObsApi

    fun retrieveByDirectory(directoryId:Long):MutableList<Document>?{
        return documentRepo.findByDirectoryId(directoryId)
    }


    fun registerDocument(document: Document):Document{
        document.documentStoreReference = "wuhu"
        val huaweiData = HuaweiCloudData(document.documentName,document.documentLocation)
        huaweiCloudService.putObject(PutParams(BUCKET_NAME,document.documentName,huaweiData))
        return this.save(document)
    }
}