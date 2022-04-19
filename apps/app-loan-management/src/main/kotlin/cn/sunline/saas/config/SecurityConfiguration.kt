package cn.sunline.saas.config

import cn.sunline.saas.filter.ExternalTuneFilter
import cn.sunline.saas.filter.PermissionFilter
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
class SecurityConfiguration (private val tokenService: TokenService, private val userService: UserService) : WebSecurityConfigurerAdapter() {

    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers("/auth/login"
            ,"/menus"
            ,"/RatePlan/all"
            ,"/roles/all"
            ,"/permissions/all"
            ,"/PartnerIntegrated/Retrieve")
    }

    override fun configure(http: HttpSecurity?) {
        http!!
                .cors().and()
                .httpBasic().disable().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .anyRequest().authenticated().and()
                .addFilterBefore(TenantDomainFilter(), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(ExternalTuneFilter(), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterBefore(AuthenticationFilter(tokenService, userService), UsernamePasswordAuthenticationFilter::class.java)
                .addFilterAfter(PermissionFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }
}