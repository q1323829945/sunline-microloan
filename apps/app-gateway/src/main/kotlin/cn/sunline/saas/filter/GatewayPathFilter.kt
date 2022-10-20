package cn.sunline.saas.filter

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.filter.exception.FilterException
import mu.KotlinLogging
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
@Order(1)
class GatewayPathFilter: Filter {
    var logger = KotlinLogging.logger {}

    val ignoreStartsWith = mutableListOf(
        "/instance","/api"
        ,"/test","/healthz","/dapr","/actors","/test","/webhook","/server"
    )

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        logger.info { "url:${httpServletRequest.requestURI}" }

        ignoreStartsWith.forEach {
            if(httpServletRequest.requestURI.startsWith(it)){
                httpServletRequest.getRequestDispatcher(httpServletRequest.requestURI).forward(request, response)
                return
            }
        }

        httpServletRequest.getHeader("client_id")?: run {
            FilterException.handleException(response!!, ManagementExceptionCode.AUTHORIZATION_TOKEN_VALIDATION_FAILED, "client_id undefined!")
            return
        }

        httpServletRequest.getHeader("access_key")?: run {
            FilterException.handleException(response!!, ManagementExceptionCode.AUTHORIZATION_TOKEN_VALIDATION_FAILED, "access_key undefined!")
            return
        }

        chain?.doFilter(request, response)
    }

}