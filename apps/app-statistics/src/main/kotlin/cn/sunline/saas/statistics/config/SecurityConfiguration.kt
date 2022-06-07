package cn.sunline.saas.statistics.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
class SecurityConfiguration () : WebSecurityConfigurerAdapter() {


    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers("/auth/login","/users","/**","/snowflake","ConsumerLoan/**")
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