package cn.sunline.saas.global.filter

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.servlet.Filter

/**
 * @title: FilterRegistration
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/22 10:42
 */
@Configuration
class FilterRegistration {

    @Bean
    fun handlerRequestHeaderFilterRegistration(handlerRequestHeaderFilter: HandlerRequestHeaderFilter): FilterRegistrationBean<Filter> {
        val filterRegistrationBean = FilterRegistrationBean<Filter>()
        filterRegistrationBean.filter = handlerRequestHeaderFilter
        filterRegistrationBean.addUrlPatterns("/*")

        return filterRegistrationBean

    }
}