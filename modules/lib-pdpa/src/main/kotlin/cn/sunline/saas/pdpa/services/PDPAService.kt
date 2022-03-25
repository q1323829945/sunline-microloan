package cn.sunline.saas.pdpa.services

import cn.sunline.saas.huaweicloud.config.HuaweiCloudTools
import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class PDPAService{

    @Autowired
    private lateinit var obsApi: ObsApi

    @Autowired
    private lateinit var huaweiCloudTools: HuaweiCloudTools


    fun sign(customerId:Long,pdpaTemplateId: Long,fileName:String,inputStream: InputStream):String{
        val key = "$customerId/signature/$pdpaTemplateId/$fileName"
        obsApi.putObject(PutParams(huaweiCloudTools.bucketName,key,inputStream))

        return key
    }

}