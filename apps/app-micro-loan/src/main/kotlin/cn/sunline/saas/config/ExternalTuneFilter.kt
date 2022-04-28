package cn.sunline.saas.config

import cn.sunline.saas.exceptions.ManagementExceptionCode
import org.springframework.http.MediaType
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ExternalTuneFilter : GenericFilterBean()  {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val httpServletRequest = request as HttpServletRequest

        val externalTune = httpServletRequest.getHeader("ExternalTune")

        externalTune?: run {
            handleException(
                response!!,
                ManagementExceptionCode.AUTHORIZATION_TOKEN_VALIDATION_FAILED,
                "Authentication validation failed"
            )
            return
        }

        request.getRequestDispatcher(httpServletRequest.servletPath).forward(request,response)
    }

    private fun handleException(response: ServletResponse, exceptionCode: ManagementExceptionCode, exceptionMessage: String) {
        val httpServletResponse = response as HttpServletResponse
        httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
        httpServletResponse.status = HttpServletResponse.SC_BAD_REQUEST
//        AuthenticationException
        httpServletResponse.outputStream.println("{\"exceptionMessage\": \"$exceptionMessage\", \"statusCode\": ${exceptionCode.code} }")
    }

}



