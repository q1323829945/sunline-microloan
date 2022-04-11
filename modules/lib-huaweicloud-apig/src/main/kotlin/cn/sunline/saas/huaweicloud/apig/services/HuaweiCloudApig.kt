package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.huaweicloud.apig.config.HuaweiCloudApigConfig
import cn.sunline.saas.redis.services.RedisClient
import cn.sunline.saas.HttpConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value

@Component
class HuaweiCloudApig {
    protected val logger = LoggerFactory.getLogger(HuaweiCloudApig::class.java)

    @Autowired
    protected lateinit var huaweiCloudApigConfig:HuaweiCloudApigConfig

    @Autowired
    protected lateinit var httpConfig: HttpConfig

    @Autowired
    protected lateinit var redisClient: RedisClient


    @Value("\${huawei.cloud.apig.region}")
    lateinit var region:String

    protected fun getUri(path:String):String{
        return "https://apig.${region}.myhuaweicloud.com$path"
    }

    protected fun getHeaderMap():MutableMap<String,String>{
        val headerMap = mutableMapOf<String, String>()
        headerMap["Content-Type"] = MediaType.APPLICATION_JSON_VALUE
        headerMap["X-Auth-Token"] = huaweiCloudApigConfig.getToken()

        return headerMap
    }
}