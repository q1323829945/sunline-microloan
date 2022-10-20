package cn.sunline.saas.filter.exception

import cn.sunline.saas.exceptions.ManagementExceptionCode
import org.springframework.http.MediaType
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletResponse

object FilterException {
    fun handleException(response: ServletResponse, exceptionCode: ManagementExceptionCode, message: String) {
        val httpServletResponse = response as HttpServletResponse
        httpServletResponse.contentType = MediaType.APPLICATION_JSON_VALUE
        httpServletResponse.status = HttpServletResponse.SC_BAD_REQUEST
        httpServletResponse.outputStream.println("{\"exceptionMessage\": \"$message\", \"statusCode\": ${exceptionCode.code} }")
    }
}