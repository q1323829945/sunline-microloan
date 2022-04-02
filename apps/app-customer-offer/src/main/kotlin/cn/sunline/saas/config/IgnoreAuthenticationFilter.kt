package cn.sunline.saas.config

import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class IgnoreAuthenticationFilter: GenericFilterBean()  {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val httpServletRequest = request as HttpServletRequest

        request.getRequestDispatcher(httpServletRequest.servletPath).forward(request,response)
    }
}