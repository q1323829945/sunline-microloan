package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.HttpConfig
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.config.HuaweiCloudApigConfig
import cn.sunline.saas.redis.services.RedisClient
import com.google.gson.Gson
import org.apache.commons.httpclient.methods.RequestEntity
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
class HuaweiCloudApig {
    protected val logger: Logger = LoggerFactory.getLogger(HuaweiCloudApig::class.java)

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


    protected fun sendClient(uri: String, httpRequestMethod: HttpRequestMethod, body: Any? = null):String{
        var entity: RequestEntity? = null
        body?.run {
            logger.debug(body.toString())
            entity = StringRequestEntity(Gson().toJson(this), MediaType.APPLICATION_JSON_VALUE, "utf-8")
        }

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(httpRequestMethod, uri, getHeaderMap(), entity)

        //sendClint
        httpConfig.sendClient(httpMethod)

        //get responseBody
        return httpConfig.getResponseBody(httpMethod)
    }
}