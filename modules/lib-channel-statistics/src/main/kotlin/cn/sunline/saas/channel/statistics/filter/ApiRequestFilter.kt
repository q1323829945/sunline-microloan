package cn.sunline.saas.channel.statistics.filter
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.channel.statistics.services.ApiDetailService
import cn.sunline.saas.global.util.setUUID
import cn.sunline.saas.multi_tenant.services.TenantService
import mu.KotlinLogging
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.*
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

@Component
@Order(1)
class ApiRequestFilter(val tenantService: TenantService): Filter {
    var logger = KotlinLogging.logger {}

    private val whiteList = mutableListOf<String>(
//        "/menus"
    )

    @Autowired
    private lateinit var apiDetailService: ApiDetailService

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {

        val httpServletRequest = request as HttpServletRequest

        httpServletRequest.getHeader(Header.TENANT_AUTHORIZATION.key)?.run {
            ContextUtil.setUUID(this)
            val tenant = try{
                tenantService.findByUUID(UUID.fromString(this))
            } catch (e:Exception){
                logger.error{ e.message }
                null
            }
            tenant?.run {
                ContextUtil.setTenant(this.id.toString())

                if(whiteList.contains(httpServletRequest.requestURI)){
                    apiDetailService.saveApiDetail(httpServletRequest.requestURI)
                }
            }
        }



        chain?.doFilter(request,response)
    }
}