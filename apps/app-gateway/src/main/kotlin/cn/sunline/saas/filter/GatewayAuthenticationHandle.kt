package cn.sunline.saas.filter

import cn.sunline.saas.client.Client
import cn.sunline.saas.client.RoutingDelegate
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.filter.exception.FilterException
import cn.sunline.saas.modules.dto.DTOGateway
import cn.sunline.saas.modules.dto.SendContext
import cn.sunline.saas.modules.enum.FormatType
import cn.sunline.saas.tools.ApiVerificationTools
import cn.sunline.saas.tools.RSASecretTools
import mu.KotlinLogging
import org.apache.catalina.core.ApplicationPart
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
@Order(2)
class GatewayAuthenticationHandle(private val client: Client): Filter {
    var logger = KotlinLogging.logger {}

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest

        val clientId = httpServletRequest.getHeader("client_id")
        val accessKey = httpServletRequest.getHeader("access_key")

        val path = httpServletRequest.requestURI
        val gateway = ApiVerificationTools.verificationAndGet(clientId,path,httpServletRequest.method)?: run{
            FilterException.handleException(response!!, ManagementExceptionCode.AUTHORIZATION_TOKEN_VALIDATION_FAILED, "Tenant/Api is not register!!")
            return
        }
        if(!RSASecretTools.checkKey(accessKey,gateway.secretKey,clientId)){
            FilterException.handleException(response!!, ManagementExceptionCode.AUTHORIZATION_TOKEN_VALIDATION_FAILED, "accessKey error,please check your accessKey!!")
            return
        }
        RoutingDelegate(client,setSendContext(httpServletRequest,gateway)).response(response!!)
    }

    private fun setSendContext(httpServletRequest: HttpServletRequest, gateway: DTOGateway): SendContext {
        val parts = mutableListOf<ApplicationPart>()
        var formatType = FormatType.Json
        if(httpServletRequest.contentType != null && httpServletRequest.contentType.startsWith("multipart/form-data")){
            formatType = FormatType.FormatData
            httpServletRequest.parts.toMutableList().forEach {
                parts.add(it as ApplicationPart)
            }
        }

        val headers = mutableMapOf<String,String>()
        httpServletRequest.headerNames.asIterator().forEach {
            headers[it] = httpServletRequest.getHeader(it)
        }
        return SendContext(
            url = gateway.url,
            path = gateway.path,
            server = gateway.server,
            query = httpServletRequest.parameterMap.mapValues { it.value[0] }.ifEmpty { null },
            headers = headers,
            parts = parts.ifEmpty { null },
            method = httpServletRequest.method,
            formatType = formatType,
            body = httpServletRequest.inputStream
        )
    }

}