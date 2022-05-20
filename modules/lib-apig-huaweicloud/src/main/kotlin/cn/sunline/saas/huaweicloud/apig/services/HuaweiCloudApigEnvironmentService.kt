package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.GatewayEnvironment
import cn.sunline.saas.gateway.api.dto.*
import cn.sunline.saas.global.constant.HttpRequestMethod
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import java.net.URLEncoder

@Service
class HuaweiCloudApigEnvironmentService:GatewayEnvironment,HuaweiCloudApig() {
    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713052.html
     */
    override fun create(environmentCreateParams: EnvironmentCreateParams): EnvironmentResponseParams {
        //uri
        val uri = getUri("/v1.0/apigw/envs")

        //get responseBody
        val responseBody = execute(uri,HttpRequestMethod.POST,environmentCreateParams)

        val map = objectMapper.readValue<Map<*,*>>(responseBody)

        return EnvironmentResponseParams(
            id = map["id"].toString()
        )
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713053.html
     */
    override fun update(environmentUpdateParams: EnvironmentUpdateParams): EnvironmentResponseParams {
        //uri
        val uri = getUri("/v1.0/apigw/envs/${environmentUpdateParams.id}")

        //get responseBody
        val responseBody = execute(uri,HttpRequestMethod.PUT,environmentUpdateParams)

        val map = objectMapper.readValue<Map<*,*>>(responseBody)

        return EnvironmentResponseParams(
            id = map["id"].toString()
        )
    }

    override fun delete(id: String) {
        //uri
        val uri = getUri("/v1.0/apigw/envs/$id")

        execute(uri,HttpRequestMethod.DELETE)
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713055.html
     */
    override fun getPaged(environmentPagedParams: EnvironmentPagedParams): EnvironmentPagedResponseParams {
        //uri
        val uri = getUri("/v1.0/apigw/envs?name=${URLEncoder.encode(environmentPagedParams.name, "utf-8")}")

        //get responseBody
        val responseBody = execute(uri,HttpRequestMethod.GET)

        val map = objectMapper.readValue<Map<*,*>>(responseBody)

        val list = map["envs"] as List<*>

        val envs = list.map {
            it as Map<*,*>
            EnvironmentResponseParams(
                id = it["id"].toString()
            )
        }

        return EnvironmentPagedResponseParams(
            total = (map["total"] as Int),
            size = (map["size"] as Int),
            envs = envs
        )
    }
}
