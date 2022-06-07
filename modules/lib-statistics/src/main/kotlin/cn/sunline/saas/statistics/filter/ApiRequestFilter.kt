package cn.sunline.saas.statistics.filter
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.statistics.services.ApiDetailService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
@Order(1)
class ApiRequestFilter: Filter {
    protected val logger: Logger = LoggerFactory.getLogger(ApiRequestFilter::class.java)

    private val whiteList = mutableListOf(
        "/menus"
    )

    @Autowired
    private lateinit var apiDetailService: ApiDetailService

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val httpServletRequest = request as HttpServletRequest

        httpServletRequest.getHeader(Header.TENANT_AUTHORIZATION.key)?.run {
            ContextUtil.setTenant(this)
        }

        if(whiteList.contains(httpServletRequest.requestURI)){
            apiDetailService.saveApiDetail(httpServletRequest.requestURI)
        }

        chain?.doFilter(request,response)
    }
}