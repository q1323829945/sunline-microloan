package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.HttpConfig
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.config.HuaweiCloudApigConfig
import cn.sunline.saas.redis.services.RedisClient
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.RequestBody
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

    protected val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    protected fun getUri(path:String):String{
        return "https://apig.${region}.myhuaweicloud.com$path"
    }

    protected fun getHeaders():MutableMap<String,String>{
        val headers = mutableMapOf<String, String>()
        headers["Content-Type"] = MediaType.APPLICATION_JSON_VALUE
        headers["X-Auth-Token"] = huaweiCloudApigConfig.getToken()

        return headers
    }


    protected fun execute(uri: String, httpRequestMethod: HttpRequestMethod, body: Any? = null):String{
        var entity: RequestBody? = null
        body?.run {
            logger.debug(body.toString())
            entity = httpConfig.setRequestBody(objectMapper.valueToTree<JsonNode>(body).toPrettyString(),MediaType.APPLICATION_JSON_VALUE)
        }

        val response = httpConfig.execute(httpRequestMethod,uri,entity,getHeaders())

        //get responseBody
        return httpConfig.getResponseBody(response)
    }
}