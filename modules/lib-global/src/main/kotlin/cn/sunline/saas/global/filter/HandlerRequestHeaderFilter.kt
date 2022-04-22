package cn.sunline.saas.global.filter

import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setUserId
import cn.sunline.saas.seq.Sequence
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest


/**
 * @title: HandlerRequestHeaderFilter
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/12 10:21
 */
@Component
class HandlerRequestHeaderFilter(private val seq: Sequence): Filter {
    protected val logger: Logger = LoggerFactory.getLogger(HandlerRequestHeaderFilter::class.java)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest

        logger.debug(httpServletRequest.requestURI)

        httpServletRequest.getHeader(Header.USER_AUTHORIZATION.name)?.run {
            ContextUtil.setUserId(this)
        }

        httpServletRequest.getHeader(Header.TENANT_AUTHORIZATION.name)?.run {
            ContextUtil.setTenant(this)
        }

        request.getRequestDispatcher(httpServletRequest.servletPath).forward(request,response)
    }

}