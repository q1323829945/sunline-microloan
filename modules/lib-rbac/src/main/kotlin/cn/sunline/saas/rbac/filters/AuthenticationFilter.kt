package cn.sunline.saas.rbac.filters

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.rbac.services.TokenService
import cn.sunline.saas.rbac.services.UserService
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter (
        private val tokenService: TokenService,
        private val userService: UserService
): GenericFilterBean() {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        val authToken = httpServletRequest.getHeader("X-Authorization")
        val authUsername = httpServletRequest.getHeader("X-Authorization-Username")?: run{
            handleException(response!!, ManagementExceptionCode.AUTHORIZATION_USERNAME_MISSING, "Missing authorization username")
            return
        }

        if (!authToken.isNullOrEmpty()) {
            val user = userService.getByUsername(authUsername)?: run {
                handleException(response!!, ManagementExceptionCode.AUTHORIZATION_USERNAME_INVALID, "Authentication username is invalid")
                return
            }

            user.run {
                tokenService.validateToken(authUsername, authToken, this.jwtKey)?: run {
                    handleException(response!!, ManagementExceptionCode.AUTHORIZATION_TOKEN_VALIDATION_FAILED, "Authentication validation failed")
                    return
                }

                val authentication = UsernamePasswordAuthenticationToken(this, null, this.authorities)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        chain?.doFilter(request, response)
    }

    private fun handleException(response: ServletResponse, exceptionCode: ManagementExceptionCode, message: String) {
        val httpServletResponse = response as HttpServletResponse
        httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
        httpServletResponse.status = HttpServletResponse.SC_BAD_REQUEST

        httpServletResponse.outputStream.println("{\"message\": \"$message\", \"code\": ${exceptionCode.code} }")
    }
}