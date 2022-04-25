package cn.sunline.saas.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfiguration () : WebSecurityConfigurerAdapter() {


    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers("/auth/login","/users","/test/**","/snowflake")
    }

    override fun configure(http: HttpSecurity?) {
        http!!
            .cors().and()
            .httpBasic().disable().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeRequests()
            .anyRequest().authenticated()
    }
}