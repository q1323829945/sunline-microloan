package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.*
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.constant.*
import com.google.gson.Gson
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.springframework.http.MediaType
import org.springframework.stereotype.Service

@Service
class HuaweiCloudApigApiService:GatewayApi,HuaweiCloudApig() {
    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713025.html
     */
    override fun register(registerParams: Any): Any? {
        if(registerParams is ApiParams){
            //uri
            val uri = getUri("/v1.0/apigw/apis")

            //body
            val body = StringRequestEntity(Gson().toJson(registerParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

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
     * https://support.huaweicloud.com/api-apig/apig-api-180713026.html
     */
    override fun update(apiUpdateParams: Any): Any? {
        if(apiUpdateParams is ApiUpdateParams){
            //uri
            val uri = getUri("/v1.0/apigw/apis/${apiUpdateParams.api_id}")

            //body
            val body = StringRequestEntity(Gson().toJson(apiUpdateParams.apiParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

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

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713027.html
     */
    override fun delete(id: String) {
        //uri
        val uri = getUri("/v1.0/apigw/apis/$id")

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.DELETE, uri, getHeaderMap())

        //sendClint
        httpConfig.sendClient(httpMethod)
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713028.html
     */
    override fun online(onlineParams: Any) {
        if(onlineParams is OnlineParams){
            //uri
            val uri = getUri("/v1.0/apigw/apis/publish/${onlineParams.api_id}")

            //body
            val body = StringRequestEntity(Gson().toJson(onlineParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.POST, uri, getHeaderMap(), body)

            //sendClint
            httpConfig.sendClient(httpMethod)
        }

    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713029.html
     */
    override fun offline(offlineParams: Any) {
        if(offlineParams is OfflineParams){
            //uri
            val uri = getUri("/v1.0/apigw/apis/publish/${offlineParams.api_id}?env_id=${offlineParams.env_id}")

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.DELETE, uri, getHeaderMap())

            //sendClint
            httpConfig.sendClient(httpMethod)
        }
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-181219254.html
     */
    override fun batchPublish(batchPublishParams: Any) {
        if(batchPublishParams is BatchPublishParams){
            //uri
            val uri = getUri("/v1.0/apigw/apis/publish?action=${batchPublishParams.action}")

            //body
            val body = StringRequestEntity(Gson().toJson(batchPublishParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.POST, uri, getHeaderMap(), body)

            //sendClint
            httpConfig.sendClient(httpMethod)
        }
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713031.html
     */
    override fun getPaged(pageParams:Any): Any? {

        if(pageParams is ApiPageParams){
            //uri
            val uri = getUri(getPagePath(pageParams))

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.GET, uri, getHeaderMap())

            //sendClint
            httpConfig.sendClient(httpMethod)

            //get responseBody
            val responseBody = httpConfig.getResponseBody(httpMethod)

            val map = Gson().fromJson(responseBody, Map::class.java)

            val list = map["apis"] as List<*>

            val responseList = list.map {
                it as Map<*,*>
                ApiResponsePage(
                    id = it["id"].toString(),
                    name = it["name"].toString(),
                    group_id = it["run_env_id"].toString(),
                    run_env_id = it["run_env_id"]?.toString(),
                    req_uri =  it["req_uri"].toString()
                )

            }

            return responseList
        }

        return null
    }

    private fun getPagePath(pageParams:ApiPageParams):String{
        var path = "/v1.0/apigw/apis?"
        pageParams.page_no?.run {
            path = "$path&page_no=${this}"
        }
        pageParams.page_size?.run {
            path = "$path&page_size=${this}"
        }
        pageParams.id?.run {
            path = "$path&id=${this}"
        }
        pageParams.name?.run {
            path = "$path&name=${this}"
        }
        pageParams.group_id?.run {
            path = "$path&group_id=${this}"
        }
        pageParams.env_id?.run {
            path = "$path&env_id=${this}"
        }

        if(path == "/v1.0/apigw/apis?"){
            path = "/v1.0/apigw/apis"
        }

        return path
    }

}