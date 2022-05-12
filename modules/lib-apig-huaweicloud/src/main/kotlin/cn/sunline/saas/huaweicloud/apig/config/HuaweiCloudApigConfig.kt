package cn.sunline.saas.huaweicloud.apig.config

import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.constant.*
import cn.sunline.saas.redis.services.RedisClient
import cn.sunline.saas.HttpConfig
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

/**
 *
 */

const val HUAWEI_CLOUD_IAM_TOKEN_HASH = "huawei_cloud_iam_token_hash"
const val HUAWEI_CLOUD_IAM_TOKEN_KEY = "huawei_cloud_iam_token_key"

@Component
class HuaweiCloudApigConfig(private val httpConfig: HttpConfig, private var redisClient: RedisClient) {

    @Value("\${huawei.cloud.iam.domainUserName}")
    lateinit var domainUserName:String
    @Value("\${huawei.cloud.iam.username}")
    lateinit var username:String
    @Value("\${huawei.cloud.iam.password}")
    lateinit var password:String
    @Value("\${huawei.cloud.iam.projectId}")
    lateinit var projectId:String


    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun getToken(): String {
        val token = redisClient.getMapItem<String>(HUAWEI_CLOUD_IAM_TOKEN_HASH, HUAWEI_CLOUD_IAM_TOKEN_KEY)

        token?.run {
            return this
        }

        return getTokenFromHuaweiCloud()
    }

    /**
     * get iam token https://support.huaweicloud.com/api-iam/iam_30_0001.html
     */
    private fun getTokenFromHuaweiCloud():String{
        //uri
        val uri = "https://iam.myhuaweicloud.com/v3/auth/tokens"

        //header
        val headers = mutableMapOf<String, String>()
//        headers["Content-Type"] = "application/json;charset=utf8"
        headers["Content-Type"] = MediaType.APPLICATION_JSON_VALUE

        val body = objectMapper.valueToTree<JsonNode>(getBody()).toPrettyString()

        val response = httpConfig.execute(HttpRequestMethod.POST,uri,httpConfig.setRequestBody(body,MediaType.APPLICATION_JSON_VALUE),headers)

        //get headers
        val responseHeaders = httpConfig.getHeader(response)

        //get token
        val responseToken = responseHeaders["X-Subject-Token"]

        redisClient.addToMap(HUAWEI_CLOUD_IAM_TOKEN_HASH, HUAWEI_CLOUD_IAM_TOKEN_KEY,responseToken,24 * 60 -5)

        return responseToken!!
    }


    private fun getBody(): BodyParams {
        return BodyParams(
            Auth(
                Identity(
                    password = Password(
                        User(
                            UserDomain(
                                domainUserName
                            ),
                            username,
                            password
                        )
                    )
                ),
                Scope(
                    project = Project(
                        id = projectId
                    )
                )
            )
        )
    }
}