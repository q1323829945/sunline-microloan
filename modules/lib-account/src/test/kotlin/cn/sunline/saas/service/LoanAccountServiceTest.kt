package cn.sunline.saas.service

import cn.sunline.saas.account.model.dto.DTOAccountAdd
import cn.sunline.saas.account.model.dto.DTOAccountBalanceChange
import cn.sunline.saas.account.service.LoanAccountService
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.seq.Sequence
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoanAccountServiceTest {
    @Autowired
    private lateinit var loanAccountService: LoanAccountService

    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantService: TenantService


    @BeforeAll
    fun init(){
        ContextUtil.setTenant("1")

        tenantService.save(
            Tenant(
                id = 1,
                country = CountryType.CHN,
                name = "admin"
            )
        )

        val account = loanAccountService.initialAccount(
            dtoAccountAdd = DTOAccountAdd(
                id = 1,
                purpose = null,
                currency = CurrencyType.CNY,
                amount = "1000000",
                date = "20220711",
                businessUnit = 1,
                customerId = 1
            )
        )

        Assertions.assertThat(account).isNotNull
        Assertions.assertThat(account.accountBalance[0].accountBalance).isEqualTo(BigDecimal(1000000))
    }


    @Test
    fun `repay account`(){
        val account = loanAccountService.repayAccount(
            dtoAccountChange = DTOAccountBalanceChange(
                id = 1,
                purpose = null,
                currency = CurrencyType.CNY,
                date = "20220711",
                amount = "400000",
            )
        )
        Assertions.assertThat(account).isNotNull
        Assertions.assertThat(account.accountBalance[0].accountBalance).isEqualTo(BigDecimal(600000).setScale(2))
    }

    @Test
    fun `get account not null`(){
        val account = loanAccountService.getAccount(1)
        Assertions.assertThat(account).isNotNull
    }

    @Test
    fun `get account is null`(){
        val account = loanAccountService.getAccount(2)
        Assertions.assertThat(account).isNull()
    }

    @Test
    fun `overdue principal`(){
        val account = loanAccountService.overduePrincipal(
            dtoAccountChange = DTOAccountBalanceChange(
                id = 1,
                purpose = null,
                currency = CurrencyType.CNY,
                date = "20220712",
                amount = "20000",
            )
        )

        Assertions.assertThat(account).isNotNull
        Assertions.assertThat(account.accountBalance[0].accountBalance).isEqualTo(BigDecimal(1000000).setScale(2))

    }
}