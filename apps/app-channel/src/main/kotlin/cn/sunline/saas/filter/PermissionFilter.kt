package cn.sunline.saas.filter

import cn.sunline.saas.config.PermissionConfig
import cn.sunline.saas.exceptions.ManagementException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.response.outputErrorStream
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class PermissionFilter : GenericFilterBean() {
    companion object {
        private val permissionRegexMap = lazy {
            val map = mutableMapOf<String, Regex>()

            PermissionConfig.values().forEach {
                map["${it.name}_${it.method.name}"] = it.resource.toRegex()
            }

            map.toMap()
        }

        fun searchForSuitablePermission(accessResource: String, requestMethod: String, authentication: Authentication) {
            authentication.authorities.firstOrNull {
                val permission = PermissionConfig.valueOf(it.authority)
                permissionRegexMap.value["${permission.name}_${requestMethod}"]?.containsMatchIn(accessResource)?: false
            } ?: throw ManagementException(ManagementExceptionCode.REQUEST_RESOURCE_ACCESS_DENIED)
        }
    }

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        val authentication = SecurityContextHolder.getContext().authentication
        val uri = httpServletRequest.requestURI

        try {
            searchForSuitablePermission(uri, httpServletRequest.method, authentication)
            chain?.doFilter(request, response)
        } catch (ex: ManagementException) {
            (response as HttpServletResponse).outputErrorStream(ex.message, ex.statusCode.code)
        }
    }
}