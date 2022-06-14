package cn.sunline.saas.filter

import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.*
import cn.sunline.saas.multi_tenant.services.TenantService
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
class HandlerRequestHeaderFilter(private val tenantService: TenantService): Filter {
    protected val logger: Logger = LoggerFactory.getLogger(HandlerRequestHeaderFilter::class.java)

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val httpServletRequest = request as HttpServletRequest

        logger.debug(httpServletRequest.requestURI)

        httpServletRequest.getHeader(Header.USER_AUTHORIZATION.key)?.run {
            ContextUtil.setUserId(this)
        }

        httpServletRequest.getHeader(Header.TENANT_AUTHORIZATION.key)?.run {
            ContextUtil.setTenant(this)

            if(ContextUtil.getPermissions().isNullOrEmpty()){
                val tenants = tenantService.getOne(this.toLong())
                val permissions = tenants?.permissions?.map {
                    it.productApplicationId
                }
                ContextUtil.setPermissions(permissions)
            }
        }

        request.getRequestDispatcher(httpServletRequest.servletPath).forward(request,response)
    }

}
