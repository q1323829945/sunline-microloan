package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.GatewayGroup
import cn.sunline.saas.gateway.api.dto.*
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
        //get responseBody
        val responseBody = sendClient(uri,HttpRequestMethod.POST,createParams)

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

        //get responseBody
        val responseBody = sendClient(uri,HttpRequestMethod.PUT,updateParams)

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

        sendClient(uri,HttpRequestMethod.DELETE)
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713022.html
     */
    override fun getPaged(groupPagedParams: GroupPagedParams): GroupPagedResponseParams {
        //uri
        val uri = getUri("/v1.0/apigw/api-groups?name=${URLEncoder.encode(groupPagedParams.name, "utf-8")}")
        //get responseBody
        val responseBody = sendClient(uri,HttpRequestMethod.GET)

        val map = Gson().fromJson(responseBody, Map::class.java)

        val list = map["groups"] as List<*>

        val groups = list.map {
            it as Map<*,*>
            GroupResponseParams(
                id = it["id"].toString()
            )
        }


        return GroupPagedResponseParams(
            total = (map["total"] as Double).toInt(),
            size = (map["size"] as Double).toInt(),
            groups = groups
        )
    }

}