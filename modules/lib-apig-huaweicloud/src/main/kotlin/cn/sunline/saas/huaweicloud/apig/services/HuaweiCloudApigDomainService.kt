package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.gateway.api.GatewayDomain
import cn.sunline.saas.gateway.api.dto.*
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.apig.constant.*
import cn.sunline.saas.huaweicloud.apig.exception.CertificateBindingException
import cn.sunline.saas.huaweicloud.apig.exception.DomainBindingException
import com.fasterxml.jackson.module.kotlin.treeToValue

const val DOMAIN_HASH_MAP = "domain_hash_map"
const val CERTIFICATE_HASH_MAP = "certificate_hash_map"

class HuaweiCloudApigDomainService:GatewayDomain,HuaweiCloudApig() {

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
        //get responseBody
        val responseBody = execute(uri,HttpRequestMethod.POST,request)

        val map = objectMapper.treeToValue<Map<*,*>>(objectMapper.readTree(responseBody))

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

            execute(uri,HttpRequestMethod.DELETE)
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

            //get responseBody
            val responseBody = execute(uri,HttpRequestMethod.POST,request)

            val map = objectMapper.treeToValue<Map<*,*>>(objectMapper.readTree(responseBody))

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

            execute(uri,HttpRequestMethod.DELETE)
        }
    }
}