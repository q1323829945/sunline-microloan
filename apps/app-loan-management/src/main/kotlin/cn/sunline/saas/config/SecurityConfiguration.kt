package cn.sunline.saas.config

import cn.sunline.saas.multi_tenant.context.TenantContext
import cn.sunline.saas.multi_tenant.filter.TenantDomainFilter
import cn.sunline.saas.rbac.filters.AuthenticationFilter
import cn.sunline.saas.rbac.services.TokenService
import cn.sunline.saas.rbac.services.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfiguration (private val tokenService: TokenService, private val userService: UserService,private val tenantContext: TenantContext) : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers("/auth/login","/menus","/DocumentTemplate/download/**","/LoanProduct/**/retrieve")
    }

    override fun configure(http: HttpSecurity?) {
        http!!
                .cors().and()
                .httpBasic().disable().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .anyRequest().authenticated().and()
                .addFilterBefore(AuthenticationFilter(tokenService, userService), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(TenantDomainFilter(tenantContext), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterAfter(PermissionFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}