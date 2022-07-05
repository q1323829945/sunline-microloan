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
class SecurityConfiguration (private val tokenService: TokenService,
                             private val userService: UserService) : WebSecurityConfigurerAdapter() {


    override fun configure(web: WebSecurity?) {
        web!!.ignoring().antMatchers(//base
            "/auth/login","/dapr/**","/error","/healthz","/actors/**"
        ).regexMatchers( //rpc
            "/LoanProduct/[0-9]+","/UnderwritingManagement/[0-9]+","ConsumerLoan/LoanAgreement/[0-9]+",
            "/InterestRate/all(\\?.*|\$)","/BusinessUnit/[0-9]+","Person/[0-9]+","/CustomerOffer/invoke/[0-9]+",
            "/LoanProduct/invoke/[0-9]+","/LoanProduct/uploadConfig/[0-9]+","/LoanProduct/interestRate/[0-9]+",
            "/pdpa/(.+)/retrieve","/LoanProduct/(.+)/retrieve","/RatePlan/invokeAll/(\\?.*|\$)","/PartnerIntegrated/Retrieve",
            "/RatePlan/[0-9]+","/RatePlan/all(\\?.*|\$)","/ApiStatistics(\\?.*|\$)","/BusinessStatistics(\\?.*|\$)",
            "/CustomerStatistics(\\?.*|\$)","/LoanProduct/invoke/list(\\?.*|\$)"
        ).regexMatchers( //pub-sub
            "/CustomerOffer/rejected","/BusinessStatistics","/CustomerStatistics","/CustomerOffer/approval",
            "/ConsumerLoan/LoanAgreement/Initiate","/PositionKeeping","/Underwriting/Initiate",
            "/ConsumerLoan/LoanAgreement/Paid","/ConsumerLoan/LoanAgreement/Signed","/tenant",
            "/ConsumerLoan/invoice/finish/[0-9]+","/ConsumerLoan/invoice/cancel/[0-9]+"
        ).antMatchers( //test
            "/test/**","/test"
        )
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