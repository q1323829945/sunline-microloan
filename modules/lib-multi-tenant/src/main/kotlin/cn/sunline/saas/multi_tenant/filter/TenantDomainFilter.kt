package cn.sunline.saas.multi_tenant.filter

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import org.springframework.http.MediaType
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TenantDomainFilter : GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        var domain = httpServletRequest.getHeader(Header.TENANT_AUTHORIZATION.key) ?: run {
            handleException(
                response!!,
                ManagementExceptionCode.AUTHORIZATION_TENANT_DOMAIN_MISSING,
                "Missing tenant domain"
            )
            return
        }

        ContextUtil.setTenant(domain)
        chain?.doFilter(request, response)
    }

    private fun handleException(response: ServletResponse, exceptionCode: ManagementExceptionCode, message: String) {
        val httpServletResponse = response as HttpServletResponse
        httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
        httpServletResponse.status = HttpServletResponse.SC_BAD_REQUEST

        httpServletResponse.outputStream.println("{\"exceptionMessage\": \"$message\", \"statusCode\": ${exceptionCode.code} }")
    }
}