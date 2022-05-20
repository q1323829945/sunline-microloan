package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.*
import cn.sunline.saas.gateway.api.constant.*
import cn.sunline.saas.gateway.api.dto.*
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.constant.*
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service

@Service
class HuaweiCloudApigApiService:GatewayApi,HuaweiCloudApig() {
    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713025.html
     */
    override fun register(registerParams: APiCreateParams): ApiResponseParams {
        //uri
        val uri = getUri("/v1.0/apigw/apis")

        val apiParams = getApiParams(
            registerParams.apiName,
            registerParams.backendUri,
            registerParams.reqMethodType,
            registerParams.groupId,
            registerParams.domainUrl,
            registerParams.authType!!,
            registerParams.remark
            )

        //get responseBody
        val responseBody = execute(uri,HttpRequestMethod.POST,apiParams)

        val map = objectMapper.readValue<Map<*,*>>(responseBody)

        return ApiResponseParams(
            id = map["id"].toString()
        )
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713026.html
     */
    override fun update(apiUpdateParams: APiUpdateParams): ApiResponseParams {
        //uri
        val uri = getUri("/v1.0/apigw/apis/${apiUpdateParams.id}")

        val apiParams = getApiParams(
            apiUpdateParams.apiName,
            apiUpdateParams.backendUri,
            apiUpdateParams.reqMethodType,
            apiUpdateParams.groupId,
            apiUpdateParams.domainUrl,
            apiUpdateParams.authType!!,
            apiUpdateParams.remark,
        )

        //get responseBody
        val responseBody = execute(uri,HttpRequestMethod.PUT,apiParams)

        val map = objectMapper.readValue<Map<*,*>>(responseBody)

        return ApiResponseParams(
            id = map["id"].toString()
        )
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713027.html
     */
    override fun delete(id: String) {
        //uri
        val uri = getUri("/v1.0/apigw/apis/$id")

        execute(uri,HttpRequestMethod.DELETE)
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713028.html
     */
    override fun online(onlineParams: OnlineParams) {
        //uri
        val uri = getUri("/v1.0/apigw/apis/publish/${onlineParams.api_id}")

        execute(uri,HttpRequestMethod.POST,onlineParams)

    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713029.html
     */
    override fun offline(offlineParams: OfflineParams) {
        //uri
        val uri = getUri("/v1.0/apigw/apis/publish/${offlineParams.api_id}?env_id=${offlineParams.env_id}")

        execute(uri,HttpRequestMethod.DELETE)
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-181219254.html
     */
    override fun batchPublish(batchPublishParams: BatchPublishParams) {
        //uri
        val uri = getUri("/v1.0/apigw/apis/publish?action=${batchPublishParams.action}")


        execute(uri,HttpRequestMethod.POST,batchPublishParams)
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713031.html
     */
    override fun getPaged(pageParams: ApiPagedParams): ApiPageResponseParams {
        //uri
        val uri = getUri(getPagePath(pageParams))


        val responseBody = execute(uri,HttpRequestMethod.GET)

        val map = objectMapper.readValue<Map<*,*>>(responseBody)

        val list = map["apis"] as List<*>

        val apis = list.map {
            it as Map<*,*>
            ApiResponseParams(
                id = it["id"].toString(),
                apiName = it["name"].toString(),
                groupId = it["run_env_id"].toString(),
                envId = it["run_env_id"]?.toString(),
                backendUri =  it["req_uri"].toString()
            )
        }


        return ApiPageResponseParams(
            total = (map["total"] as Int),
            size = (map["size"] as Int),
            apis = apis
        )
    }

    private fun getPagePath(pageParams: ApiPagedParams):String{
        var path = "/v1.0/apigw/apis?"
        pageParams.pageNo?.run {
            path = "$path&page_no=${this}"
        }
        pageParams.pageSize?.run {
            path = "$path&page_size=${this}"
        }
        pageParams.id?.run {
            path = "$path&id=${this}"
        }
        pageParams.name?.run {
            path = "$path&name=${this}"
        }
        pageParams.groupId?.run {
            path = "$path&group_id=${this}"
        }
        pageParams.evnId?.run {
            path = "$path&env_id=${this}"
        }

        if(path == "/v1.0/apigw/apis?"){
            path = "/v1.0/apigw/apis"
        }

        return path
    }


    private fun getApiParams(name:String,uri:String,reqMethod: ReqMethodType,groupId:String,domainUrl:String,authType:AuthType,remark:String? = null):ApiParams{
        return ApiParams(
            group_id = groupId,
            name = name,
            type = 1,
            req_protocol = ReqProtocolEnum.HTTPS,
            req_method = reqMethod,
            req_uri = uri,
            match_mode = MatchMode.NORMAL,
            remark = remark,
            auth_type = authType,
            backend_type = BackendType.HTTP,
            cors = true,
            backend_api = BackendApi(
                url_domain = domainUrl,
                req_protocol = ReqProtocolEnum.HTTP,
                req_method = reqMethod,
                req_uri = uri,
                timeout = 60000,
                remark = remark,
                vpc_status = 2
            )
        )
    }
}