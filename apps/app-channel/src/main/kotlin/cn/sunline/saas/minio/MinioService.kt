package cn.sunline.saas.minio

import cn.sunline.saas.minio.service.MinioTemplate
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MinioService {
    @Autowired
    private lateinit var minioTemplate: MinioTemplate

    @Autowired
    private lateinit var obsApi: ObsApi

    fun minioToObs(minioFilePath:String,obsFilePath:String):String?{
        val inputStream = minioTemplate.getObject(minioFilePath)
        inputStream?.run { obsApi.putObject(PutParams(obsFilePath,inputStream)) }

        return inputStream?.run { obsFilePath }
    }

}