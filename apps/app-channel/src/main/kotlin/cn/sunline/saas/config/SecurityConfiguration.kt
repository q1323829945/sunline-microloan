package cn.sunline.saas.config

import cn.sunline.saas.filter.PermissionFilter
import cn.sunline.saas.multi_tenant.filter.TenantDomainFilter
import cn.sunline.saas.channel.rbac.filters.AuthenticationFilter
import cn.sunline.saas.channel.rbac.services.TokenService
import cn.sunline.saas.channel.rbac.services.UserService
import cn.sunline.saas.multi_tenant.services.TenantService
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfiguration (private val tokenService: TokenService,
                             private val userService: UserService,
                             private val tenantService: TenantService) : WebSecurityConfigurerAdapter() {


    override fun configure(web: WebSecurity?) {
        web!!.ignoring()
            .antMatchers("/auth/login","/dapr/**","/error","/healthz","/questionnaire","questionnaire/**",
                "/loan/record","/loan/status/**","/loan/product/**","/loan/submit/callback","/application/batch/record/loan"
                ,"/webhook","/actors/**","/**/all","/pdpa","/pdpa/**","/users/position/**")
            .regexMatchers(HttpMethod.GET,"/product(\\?.*|\$)","/test(\\?.*|\$)","/(\\?.*|\$)/all(\\?.*|\$)")
    }

    override fun configure(http: HttpSecurity?) {
        http!!
                .cors().and()
                .httpBasic().disable().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .anyRequest().authenticated().and()
                .addFilterBefore(TenantDomainFilter(tenantService), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(AuthenticationFilter(tokenService, userService), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterAfter(PermissionFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}