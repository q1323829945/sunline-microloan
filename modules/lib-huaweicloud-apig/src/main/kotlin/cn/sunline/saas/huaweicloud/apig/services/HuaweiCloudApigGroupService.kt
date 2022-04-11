package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.GatewayGroup
import cn.sunline.saas.gateway.api.dto.GroupCreateParams
import cn.sunline.saas.gateway.api.dto.GroupPagedParams
import cn.sunline.saas.gateway.api.dto.GroupResponseParams
import cn.sunline.saas.gateway.api.dto.GroupUpdateParams
import cn.sunline.saas.global.constant.HttpRequestMethod
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
    override fun create(createParams: GroupCreateParams): GroupResponseParams {
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

        return GroupResponseParams(
            id = map["id"].toString()
        )
    }

    /**
     * update api
     * https://support.huaweicloud.com/api-apig/apig-api-180713017.html
     */
    override fun update(updateParams: GroupUpdateParams): GroupResponseParams {
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

        return GroupResponseParams(
            id = map["id"].toString()
        )
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
    override fun getPaged(groupPagedParams: GroupPagedParams):List<GroupResponseParams> {
        //uri
        val uri = getUri("/v1.0/apigw/api-groups?name=${URLEncoder.encode(groupPagedParams.name, "utf-8")}")

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.GET, uri, getHeaderMap())

        //sendClint
        httpConfig.sendClient(httpMethod)

        //get responseBody
        val responseBody = httpConfig.getResponseBody(httpMethod)

        val map = Gson().fromJson(responseBody, Map::class.java)

        val list = map["groups"] as List<*>


        return list.map {
            it as Map<*,*>
            GroupResponseParams(
                id = it["id"].toString()
            )
        }
    }

}