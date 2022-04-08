package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.GatewayEnvironment
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.constant.EnvironmentCreateParams
import cn.sunline.saas.huaweicloud.apig.constant.EnvironmentUpdateParams
import cn.sunline.saas.huaweicloud.config.HttpConfig
import com.google.gson.Gson
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Service
class HuaweiCloudApigEnvironmentService:GatewayEnvironment,HuaweiCloudApig() {
    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713052.html
     */
    override fun create(environmentCreateParams: Any): Any? {
        if(environmentCreateParams is EnvironmentCreateParams){
            //uri
            val uri = getUri("/v1.0/apigw/envs")

            //body
            val body = StringRequestEntity(Gson().toJson(environmentCreateParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.POST, uri, getHeaderMap(), body)

            //sendClint
            httpConfig.sendClient(httpMethod)

            //get responseBody
            val responseBody = httpConfig.getResponseBody(httpMethod)

            val map = Gson().fromJson(responseBody, Map::class.java)

            return map["id"]
        }

        return null
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713053.html
     */
    override fun update(environmentUpdateParams: Any): Any? {

        if(environmentUpdateParams is EnvironmentUpdateParams){
            //uri
            val uri = getUri("/v1.0/apigw/envs/${environmentUpdateParams.id}")

            //body
            val body = StringRequestEntity(Gson().toJson(environmentUpdateParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.PUT, uri, getHeaderMap(), body)

            //sendClint
            httpConfig.sendClient(httpMethod)

            //get responseBody
            val responseBody = httpConfig.getResponseBody(httpMethod)

            val map = Gson().fromJson(responseBody, Map::class.java)

            return map["id"]
        }



        return null
    }

    override fun delete(id: String) {
        //uri
        val uri = getUri("/v1.0/apigw/envs/$id")

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.DELETE, uri, getHeaderMap())

        //sendClint
        httpConfig.sendClient(httpMethod)
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713055.html
     */
    override fun getOne(environmentName: String): Any? {
        //uri
        val uri = getUri("/v1.0/apigw/envs?name=${URLEncoder.encode(environmentName, "utf-8")}")

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.GET, uri, getHeaderMap())

        //sendClint
        httpConfig.sendClient(httpMethod)

        //get responseBody
        val responseBody = httpConfig.getResponseBody(httpMethod)

        val map = Gson().fromJson(responseBody, Map::class.java)

        val list = map["envs"] as List<*>

        if(list.isNotEmpty()){
            return (list[0] as Map<*, *>)["id"]
        }


        return null
    }
}
