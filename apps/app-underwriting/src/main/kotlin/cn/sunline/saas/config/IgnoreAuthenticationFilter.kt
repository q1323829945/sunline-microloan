package cn.sunline.saas.config

import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class IgnoreAuthenticationFilter: GenericFilterBean()  {
    private val logger: Logger = LoggerFactory.getLogger(IgnoreAuthenticationFilter::class.java)

    val traceparent = "traceparent"
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val httpServletRequest = request as HttpServletRequest

        request.getRequestDispatcher(httpServletRequest.servletPath).forward(request,response)

        httpServletRequest.getHeader(Header.TENANT_AUTHORIZATION.name)?:run {
            //TODO:说明是mq方式调用
        }

        chain?.doFilter(request, response)
    }
}