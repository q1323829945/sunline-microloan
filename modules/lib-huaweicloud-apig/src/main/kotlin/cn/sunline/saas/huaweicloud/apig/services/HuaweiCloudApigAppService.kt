package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.GatewayApp
import cn.sunline.saas.gateway.api.dto.*
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
    override fun create(appCreateParams: AppCreateParams): AppResponseParams {
        //uri
        val uri = getUri("/v1.0/apigw/apps")

        val request = HuaweiCloudAppCreateParams(
            name = appCreateParams.name,
            remark = appCreateParams.remark,
            app_key = appCreateParams.appKey,
            app_secret = appCreateParams.appSecret
        )
        //body
        val body = StringRequestEntity(Gson().toJson(request), MediaType.APPLICATION_JSON_VALUE, "utf-8")

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
            appKey = map["app_key"].toString(),
            appSecret = map["app_secret"].toString()
        )
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713037.html
     */
    override fun update(appUpdateParams: AppUpdateParams):AppResponseParams {
        //uri
        val uri = getUri("/v1.0/apigw/apps/${appUpdateParams.id}")

        val request = HuaweiCloudAppUpdateParams(
                id = appUpdateParams.id,
                name = appUpdateParams.name,
                remark = appUpdateParams.remark,
                app_key = appUpdateParams.appKey,
                app_secret = appUpdateParams.appSecret
            )

        //body
        val body = StringRequestEntity(Gson().toJson(request), MediaType.APPLICATION_JSON_VALUE, "utf-8")

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
            appKey = map["app_key"].toString(),
            appSecret = map["app_secret"].toString()
        )
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
    override fun auths(authsParams: AuthsParams): List<AppAuthsResponseParams> {
        //uri
        val uri = getUri("/v1.0/apigw/app-auths")

        val request = HuaweiCloudAuthsParams(
            api_ids = authsParams.apiIds,
            app_ids = authsParams.appIds,
            env_id = authsParams.envId
        )
        //body
        val body = StringRequestEntity(Gson().toJson(request), MediaType.APPLICATION_JSON_VALUE, "utf-8")

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.POST, uri, getHeaderMap(), body)

        //sendClint
        httpConfig.sendClient(httpMethod)

        //get responseBody
        val responseBody = httpConfig.getResponseBody(httpMethod)

        val list = Gson().fromJson(responseBody, List::class.java)

        return list.map {
            it as Map<*,*>
            AppAuthsResponseParams(
                id = it["id"].toString(),
                appId = it["app_id"].toString(),
                apiId = it["api_id"].toString(),
                authResult = it["auth_result"].toString()
            )
        }
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

    /**
     *   https://support.huaweicloud.com/api-apig/apig-api-180713042.html
     */
    override fun getPaged(appPagedParams: AppPagedParams):List<AppResponseParams> {
        //uri
        val uri = getUri("/v1.0/apigw/apps?name=${URLEncoder.encode(appPagedParams.name, "utf-8")}")

        //get httpMethod
        val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.GET, uri, getHeaderMap())

        //sendClint
        httpConfig.sendClient(httpMethod)

        //get responseBody
        val responseBody = httpConfig.getResponseBody(httpMethod)

        val map = Gson().fromJson(responseBody, Map::class.java)

        val list = map["apps"] as List<*>

        return list.map {
            it as Map<*,*>
            AppResponseParams(
                id = it["id"].toString(),
                name = it["name"].toString(),
                appKey = it["app_key"].toString(),
                appSecret = it["app_secret"].toString(),
            )
        }
    }
}