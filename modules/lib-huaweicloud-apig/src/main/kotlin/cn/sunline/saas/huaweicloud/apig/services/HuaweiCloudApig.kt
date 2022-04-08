package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.huaweicloud.apig.config.HuaweiCloudApigConfig
import cn.sunline.saas.huaweicloud.config.HttpConfig
import cn.sunline.saas.redis.services.RedisClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class HuaweiCloudApig {
    @Autowired
    protected lateinit var huaweiCloudApigConfig:HuaweiCloudApigConfig

    @Autowired
    protected lateinit var httpConfig: HttpConfig

    @Autowired
    protected lateinit var redisClient: RedisClient



    protected fun getUri(path:String):String{
        return "https://apig.cn-east-3.myhuaweicloud.com$path"
    }

    protected fun getHeaderMap():MutableMap<String,String>{
        val headerMap = mutableMapOf<String, String>()
        headerMap["Content-Type"] = MediaType.APPLICATION_JSON_VALUE
        headerMap["X-Auth-Token"] = huaweiCloudApigConfig.getToken()

        return headerMap
    }
}