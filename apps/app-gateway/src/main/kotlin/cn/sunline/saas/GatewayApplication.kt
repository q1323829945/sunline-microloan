package cn.sunline.saas

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.util.ResourceUtils
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
class GatewayApplication: WebMvcConfigurationSupport() {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/gitbook/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/gitbook/")
        registry.addResourceHandler("/en/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/en/")
        registry.addResourceHandler("/zh/**").addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/zh/")

        super.addResourceHandlers(registry)
    }
}

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}
