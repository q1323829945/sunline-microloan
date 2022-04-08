package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.GatewayGroup
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.constant.GroupCreateParams
import cn.sunline.saas.huaweicloud.apig.constant.GroupUpdateParams
import com.google.gson.Gson
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import java.net.URLEncoder

@Service
class HuaweiCloudApigGroupService: GatewayGroup,HuaweiCloudApig(){
    /**
     * create api:
     * https://support.huaweicloud.com/api-apig/apig-api-180713016.html
     */
    override fun create(createParams: Any): Any? {
        if(createParams is GroupCreateParams){
            //uri
            val uri = getUri("/v1.0/apigw/api-groups")

            //body
            val body = StringRequestEntity(Gson().toJson(createParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

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
     * update api
     * https://support.huaweicloud.com/api-apig/apig-api-180713017.html
     */
    override fun update(updateParams: Any): Any? {
        if(updateParams is GroupUpdateParams){
            //uri
            val uri = getUri("/v1.0/apigw/api-groups/${updateParams.id}")

            //body
            val body = StringRequestEntity(Gson().toJson(updateParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

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
     * https://support.huaweicloud.com/api-apig/apig-api-180713018.html
     */
    override fun delete(id: String) {
        //uri
        val uri = getUri("/v1.0/apigw/api-groups/$id")

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.DELETE, uri, getHeaderMap())

        //sendClint
        httpConfig.sendClient(httpMethod)
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713022.html
     */
    override fun getOne(groupName: String): Any? {
        //uri
        val uri = getUri("/v1.0/apigw/api-groups?name=${URLEncoder.encode(groupName, "utf-8")}")

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.GET, uri, getHeaderMap())

        //sendClint
        httpConfig.sendClient(httpMethod)

        //get responseBody
        val responseBody = httpConfig.getResponseBody(httpMethod)

        val map = Gson().fromJson(responseBody, Map::class.java)

        val list = map["groups"] as List<*>

        if(list.isNotEmpty()){
            return (list[0] as Map<*, *>)["id"]
        }


        return null
    }

}