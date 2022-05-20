package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.GatewayApp
import cn.sunline.saas.gateway.api.dto.*
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.constant.*
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.fasterxml.jackson.module.kotlin.readValue
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
        //get responseBody
        val responseBody = execute(uri,HttpRequestMethod.POST,request)

        val map = objectMapper.readValue<Map<*,*>>(responseBody)

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

        val responseBody = execute(uri,HttpRequestMethod.PUT,request)

        val map = objectMapper.readValue<Map<*,*>>(responseBody)

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

        execute(uri,HttpRequestMethod.DELETE)
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
        //get responseBody
        val responseBody = execute(uri,HttpRequestMethod.POST,request)

        val list = objectMapper.readValue<List<*>>(responseBody)

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

        execute(uri,HttpRequestMethod.DELETE)
    }

    /**
     *   https://support.huaweicloud.com/api-apig/apig-api-180713042.html
     */
    override fun getPaged(appPagedParams: AppPagedParams):AppPagedResponseParams {
        //uri
        val uri = getUri("/v1.0/apigw/apps?name=${URLEncoder.encode(appPagedParams.name, "utf-8")}")

        //get responseBody
        val responseBody = execute(uri,HttpRequestMethod.GET)

        val map = objectMapper.readValue<Map<*,*>>(responseBody)

        val list = map["apps"] as List<*>

        val apps = list.map {
            it as Map<*,*>
            AppResponseParams(
                id = it["id"].toString(),
                name = it["name"].toString(),
                appKey = it["app_key"].toString(),
                appSecret = it["app_secret"].toString(),
            )
        }


        return AppPagedResponseParams(
            total = (map["total"] as Int),
            size = (map["size"] as Int),
            apps = apps
        )
    }
}

