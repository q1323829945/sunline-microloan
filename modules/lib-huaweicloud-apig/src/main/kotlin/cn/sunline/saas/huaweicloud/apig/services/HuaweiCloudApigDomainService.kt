package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.GatewayDomain
import cn.sunline.saas.gateway.api.dto.*
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.constant.*
import cn.sunline.saas.huaweicloud.apig.exception.CertificateBindingException
import cn.sunline.saas.huaweicloud.apig.exception.DomainBindingException
import com.google.gson.Gson
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType

const val DOMAIN_HASH_MAP = "domain_hash_map"
const val CERTIFICATE_HASH_MAP = "certificate_hash_map"

class HuaweiCloudApigDomainService:GatewayDomain,HuaweiCloudApig() {

    protected val logger: Logger = LoggerFactory.getLogger(HuaweiCloudApigDomainService::class.java)
    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713154.html
     */
    override fun domainBinding(domainParams: DomainBindingParams): DomainResponseParams {
        //uri
        val uri = getUri("/v1.0/apigw/api-groups/${domainParams.groupId}/domains")

        val request = HuaweiCloudDomainBindingParams(
            group_id = domainParams.groupId!!,
            url_domain = domainParams.urlDomain
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

        val status = map["status"].toString()

        if(status == "4"){
            logger.error("doaminBinding error")
            logger.error(map.toString())
            throw DomainBindingException("doaminBinding error")
        }

        val id = map["id"].toString()

        redisClient.addToMap(DOMAIN_HASH_MAP,domainParams.urlDomain,id)

        return DomainResponseParams(
            id = id
        )
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713155.html
     */
    override fun domainUnbound(domainParams: DomainUnboundParams) {
        val domainId = redisClient.getMapItem<String>(DOMAIN_HASH_MAP,domainParams.urlDomain)
        domainId?.run {
            //uri
            val uri = getUri("/v1.0/apigw/api-groups/${domainParams.groupId}/domains/$this")

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.DELETE, uri, getHeaderMap())

            //sendClint
            httpConfig.sendClient(httpMethod)
        }
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713156.html
     */
    override fun certificateBinding(certificateBindingParams: CertificateBindingParams) {
        val domainId = redisClient.getMapItem<String>(DOMAIN_HASH_MAP,certificateBindingParams.urlDomain)
        domainId?.run {
            //uri
            val uri = getUri("/v1.0/apigw/api-groups/${certificateBindingParams.urlDomain}/domains/$this/certificate")

            val request = HuaweiCloudCertificateBindingParams(
                group_id = certificateBindingParams.groupId!!,
                name = certificateBindingParams.name!!,
                cert_content = certificateBindingParams.certContent,
                private_key = certificateBindingParams.privateKey,
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

            val status = map["status"].toString()

            if(status == "4"){
                logger.error("certificateBinding error")
                logger.error(map.toString())
                throw CertificateBindingException("certificateBinding error")
            }


            redisClient.addToMap(CERTIFICATE_HASH_MAP,this,map["id"])
        }
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713158.html
     */
    override fun certificateDelete(certificateDeleteParams: CertificateDeleteParams) {
        val domainId = redisClient.getMapItem<String>(DOMAIN_HASH_MAP,certificateDeleteParams.urlDomain)
        domainId?.run {
            redisClient.getMapItem<String>(CERTIFICATE_HASH_MAP,certificateDeleteParams.urlDomain)
        }?.run {
            //uri
            val uri = getUri("/v1.0/apigw/api-groups/${certificateDeleteParams.groupId}/domains/$domainId/certificate/$this")

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.DELETE, uri, getHeaderMap())

            //sendClint
            httpConfig.sendClient(httpMethod)
        }
    }
}