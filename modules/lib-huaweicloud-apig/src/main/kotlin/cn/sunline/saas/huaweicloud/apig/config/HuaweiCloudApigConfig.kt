package cn.sunline.saas.huaweicloud.apig.config

import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.constant.*
import cn.sunline.saas.redis.services.RedisClient
import cn.sunline.saas.HttpConfig
import com.google.gson.Gson
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import java.util.*

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
        val headerMap = mutableMapOf<String, String>()
        headerMap["Content-Type"] = "application/json;charset=utf8"

        //body
        val body = StringRequestEntity(Gson().toJson(getBody()), MediaType.APPLICATION_JSON_VALUE, "utf-8")

        //get httpMethod
        val httpPost = httpConfig.getHttpMethod(HttpRequestMethod.POST, uri, headerMap, body)

        //sendClint
        httpConfig.sendClient(httpPost)

        //get headers
        val headers = httpConfig.getHeader(httpPost)

        //get token
        val responseToken = headers["X-Subject-Token"]

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

    private fun getNowTime():String{
        return DateTime.now().withZone(DateTimeZone.forID("UTC")).toString("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.ENGLISH)
    }

}
