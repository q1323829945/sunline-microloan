package cn.sunline.saas.multi_tenant.filter

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.multi_tenant.context.TenantContext
import cn.sunline.saas.multi_tenant.model.Tenant
import org.springframework.http.MediaType
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TenantDomainFilter(private val tenantContext: TenantContext) : GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        var domain = httpServletRequest.getHeader("X-Tenant-Domain") ?: run {
            handleException(
                response!!,
                ManagementExceptionCode.AUTHORIZATION_TENANT_DOMAIN_MISSING,
                "Missing tenant domain"
            )
            return
        }

        tenantContext.set(domain)
        chain?.doFilter(request, response)
    }

    private fun handleException(response: ServletResponse, exceptionCode: ManagementExceptionCode, message: String) {
        val httpServletResponse = response as HttpServletResponse
        httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
        httpServletResponse.status = HttpServletResponse.SC_BAD_REQUEST

        httpServletResponse.outputStream.println("{\"message\": \"$message\", \"code\": ${exceptionCode.code} }")
    }
}