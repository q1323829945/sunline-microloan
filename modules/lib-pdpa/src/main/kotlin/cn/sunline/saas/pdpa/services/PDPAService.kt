package cn.sunline.saas.pdpa.services

import cn.sunline.saas.obs.api.ObsApi
import cn.sunline.saas.obs.api.PutParams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.InputStream

@Service
class PDPAService{

    @Autowired
    private lateinit var obsApi: ObsApi


    fun sign(customerId:Long,pdpaTemplateId: Long,fileName:String,inputStream: InputStream):String{
        val key = "$customerId/signature/$pdpaTemplateId/$fileName"
        obsApi.putObject(PutParams(key,inputStream))

        return key
    }

}