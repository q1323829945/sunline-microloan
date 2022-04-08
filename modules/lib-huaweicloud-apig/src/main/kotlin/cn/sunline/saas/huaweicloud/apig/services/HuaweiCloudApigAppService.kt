package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.GatewayApp
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.constant.*
import com.google.gson.Gson
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import java.net.URLEncoder

@Service
class HuaweiCloudApigAppService:GatewayApp,HuaweiCloudApig() {

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713036.html
     */
    override fun create(appCreateParams: Any): Any? {
        if(appCreateParams is AppCreateParams){
            //uri
            val uri = getUri("/v1.0/apigw/apps")

            //body
            val body = StringRequestEntity(Gson().toJson(appCreateParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.POST, uri, getHeaderMap(), body)

            //sendClint
            httpConfig.sendClient(httpMethod)

            //get responseBody
            val responseBody = httpConfig.getResponseBody(httpMethod)

            val map = Gson().fromJson(responseBody, Map::class.java)

            return AppResponseParams(
                id = map["id"].toString(),
                name = map["name"].toString(),
                app_key = map["app_key"].toString(),
                app_secret = map["app_secret"].toString()
            )
        }

        return null

    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713037.html
     */
    override fun update(appUpdateParams: Any): Any? {
        if(appUpdateParams is AppUpdateParams){
            //uri
            val uri = getUri("/v1.0/apigw/apps/${appUpdateParams.id}")

            //body
            val body = StringRequestEntity(Gson().toJson(appUpdateParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.PUT, uri, getHeaderMap(), body)

            //sendClint
            httpConfig.sendClient(httpMethod)

            //get responseBody
            val responseBody = httpConfig.getResponseBody(httpMethod)

            val map = Gson().fromJson(responseBody, Map::class.java)

            return AppResponseParams(
                id = map["id"].toString(),
                name = map["name"].toString(),
                app_key = map["app_key"].toString(),
                app_secret = map["app_secret"].toString()
            )
        }

        return null
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713039.html
     */
    override fun delete(id: String) {
        //uri
        val uri = getUri("/v1.0/apigw/apps/$id")

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.DELETE, uri, getHeaderMap())

        //sendClint
        httpConfig.sendClient(httpMethod)
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713046.html
     */
    override fun auths(authsParams: Any): Any? {
        if(authsParams is AuthsParams){
            //uri
            val uri = getUri("/v1.0/apigw/app-auths")

            //body
            val body = StringRequestEntity(Gson().toJson(authsParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.POST, uri, getHeaderMap(), body)

            //sendClint
            httpConfig.sendClient(httpMethod)

            //get responseBody
            val responseBody = httpConfig.getResponseBody(httpMethod)

            val list = Gson().fromJson(responseBody, List::class.java)

            val resultList = list.map {
                it as Map<*,*>
                AppAuthsResponseParams(
                    id = it["id"].toString(),
                    app_id = it["app_id"].toString(),
                    api_id = it["api_id"].toString(),
                    auth_result = it["auth_result"].toString()
                )
            }

            println(resultList)

            return resultList
        }

        return null
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713049.html
     */
    override fun revokeAuths(id: String) {
        //uri
        val uri = getUri("/v1.0/apigw/app-auths/$id")

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.DELETE, uri, getHeaderMap())

        //sendClint
        httpConfig.sendClient(httpMethod)
    }

    override fun getOne(appName: String): Any? {
        //uri
        val uri = getUri("/v1.0/apigw/apps?name=${URLEncoder.encode(appName, "utf-8")}")

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.GET, uri, getHeaderMap())

        //sendClint
        httpConfig.sendClient(httpMethod)

        //get responseBody
        val responseBody = httpConfig.getResponseBody(httpMethod)

        val map = Gson().fromJson(responseBody, Map::class.java)

        val list = map["apps"] as List<*>

        if(list.isNotEmpty()){
            return (list[0] as Map<*, *>)["id"]
        }


        return null
    }
}