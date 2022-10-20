package cn.sunline.saas.filter

import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.*
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantMap
import mu.KotlinLogging
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
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
class HandlerRequestHeaderFilter(val tenantService: TenantService): Filter {
    var logger = KotlinLogging.logger {}

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val httpServletRequest = request as HttpServletRequest

        if(!httpServletRequest.requestURI.equals("/healthz")){
            logger.info(httpServletRequest.requestURI)
        }

        httpServletRequest.getHeader(Header.USER_AUTHORIZATION.key)?.run {
            ContextUtil.setUserId(this)
        }


        httpServletRequest.getHeader("submit")?.run {
            ContextUtil.setApplyLoanSubmit(this)
        }


        httpServletRequest.getHeader(Header.TENANT_AUTHORIZATION.key)?.run {
            TenantMap.setContextUtil(tenantService,this)
        }

        request.getRequestDispatcher(httpServletRequest.servletPath).forward(request,response)
    }

}
