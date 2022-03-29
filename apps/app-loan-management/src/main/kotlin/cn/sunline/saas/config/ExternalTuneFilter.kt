package cn.sunline.saas.config

import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class ExternalTuneFilter : GenericFilterBean()  {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val httpServletRequest = request as HttpServletRequest

        val externalTune = httpServletRequest.getHeader("ExternalTune")

        externalTune?.run {
            request.getRequestDispatcher(httpServletRequest.servletPath).forward(request,response)
            return
        }

        chain?.doFilter(request, response)
    }
}