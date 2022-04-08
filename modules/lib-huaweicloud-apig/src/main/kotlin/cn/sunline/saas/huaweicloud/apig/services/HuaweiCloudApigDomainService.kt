package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.GatewayDomain
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.constant.CertificateBindingParams
import cn.sunline.saas.huaweicloud.apig.constant.CertificateDeleteParams
import cn.sunline.saas.huaweicloud.apig.constant.DomainBindingParams
import cn.sunline.saas.huaweicloud.apig.constant.DomainUnboundParams
import cn.sunline.saas.huaweicloud.apig.exception.DNSException
import com.google.gson.Gson
import org.apache.commons.httpclient.methods.StringRequestEntity
import org.springframework.http.MediaType

const val DOMAIN_HASH_MAP = "domain_hash_map"
const val CERTIFICATE_HASH_MAP = "certificate_hash_map"

class HuaweiCloudApigDomainService:GatewayDomain,HuaweiCloudApig() {

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713154.html
     */
    override fun domainBinding(domainParams: Any):Any? {
        if(domainParams is DomainBindingParams){
            //uri
            val uri = getUri("/v1.0/apigw/api-groups/${domainParams.group_id}/domains")

            //body
            val body = StringRequestEntity(Gson().toJson(domainParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

            //get httpMethod
            val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.POST, uri, getHeaderMap(), body)

            //sendClint
            httpConfig.sendClient(httpMethod)

            //get responseBody
            val responseBody = httpConfig.getResponseBody(httpMethod)

            val map = Gson().fromJson(responseBody, Map::class.java)

            val status = map["status"].toString()

            if(status == "4"){
                throw DNSException("dns error")
            }

            val id = map["id"].toString()

            redisClient.addToMap(DOMAIN_HASH_MAP,domainParams.url_domain,id)

            return id
        }

        return null
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713155.html
     */
    override fun domainUnbound(domainParams: Any) {
        if(domainParams is DomainUnboundParams){
            val domainId = redisClient.getMapItem<String>(DOMAIN_HASH_MAP,domainParams.url_domain)
            domainId?.run {
                //uri
                val uri = getUri("/v1.0/apigw/api-groups/${domainParams.group_id}/domains/$this")

                //get httpMethod
                val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.DELETE, uri, getHeaderMap())

                //sendClint
                httpConfig.sendClient(httpMethod)
            }
        }
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713156.html
     */
    override fun certificateBinding(certificateBindingParams: Any) {
        if(certificateBindingParams is CertificateBindingParams){
            val domainId = redisClient.getMapItem<String>(DOMAIN_HASH_MAP,certificateBindingParams.url_domain)
            domainId?.run {
                //uri
                val uri = getUri("/v1.0/apigw/api-groups/${certificateBindingParams.group_id}/domains/$this/certificate")

                //body
                val body = StringRequestEntity(Gson().toJson(certificateBindingParams), MediaType.APPLICATION_JSON_VALUE, "utf-8")

                //get httpMethod
                val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.POST, uri, getHeaderMap(), body)

                //sendClint
                httpConfig.sendClient(httpMethod)

                //get responseBody
                val responseBody = httpConfig.getResponseBody(httpMethod)

                val map = Gson().fromJson(responseBody, Map::class.java)

                val status = map["status"].toString()

                if(status == "4"){
                    throw DNSException("certificate error")
                }


                redisClient.addToMap(CERTIFICATE_HASH_MAP,this,map["id"])
            }
        }
    }

    /**
     * https://support.huaweicloud.com/api-apig/apig-api-180713158.html
     */
    override fun certificateDelete(certificateDeleteParams: Any) {
        if(certificateDeleteParams is CertificateDeleteParams){
            val domainId = redisClient.getMapItem<String>(DOMAIN_HASH_MAP,certificateDeleteParams.url_domain)
            domainId?.run {
                redisClient.getMapItem<String>(CERTIFICATE_HASH_MAP,certificateDeleteParams.url_domain)
            }?.run {
                //uri
                val uri = getUri("/v1.0/apigw/api-groups/${certificateDeleteParams.group_id}/domains/$domainId/certificate/$this")

                //get httpMethod
                val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.DELETE, uri, getHeaderMap())

                //sendClint
                httpConfig.sendClient(httpMethod)
            }

        }
    }
}