package cn.sunline.saas.service

import cn.sunline.saas.account.model.AccountBalanceType
import cn.sunline.saas.account.model.AccountClass
import cn.sunline.saas.account.model.AccountStatus
import cn.sunline.saas.account.model.AccountType
import cn.sunline.saas.account.model.db.Account
import cn.sunline.saas.account.model.db.AccountBalance
import cn.sunline.saas.account.model.dto.DTOAccountAdd
import cn.sunline.saas.account.service.AccountService
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import cn.sunline.saas.seq.Sequence
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountServiceTest {
    @Autowired
    private lateinit var accountService: AccountService

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
    }

    @Test
    fun `init account`(){
        val account = accountService.initialAccount(
            accountClass = AccountClass.LOAN,
            accountType = AccountType.DEBIT_ACCOUNT,
            accountBalanceType = AccountBalanceType.AVAILABLE_BALANCE,
            dtoAccountAdd = DTOAccountAdd(
                id = sequence.nextId(),
                purpose = "",
                currency = CurrencyType.CNY,
                amount = "90000",
                date = "20220711",
                businessUnit = 1,
                customerId = 1
            )
        )

        Assertions.assertThat(account).isNotNull
        Assertions.assertThat(account.accountBalance[0].accountBalance).isEqualTo(BigDecimal(90000))

    }

    @Test
    fun `save AccountBalance`(){
        val account = accountService.saveAccountBalance(
            account = Account(
                id = sequence.nextId(),
                accountStatus = AccountStatus.ACTIVATED,
                accountType = AccountType.DEBIT_ACCOUNT,
                accountClass = AccountClass.LOAN,
                accountPurpose = "",
                accountCurrency = CurrencyType.CNY,
                accountBalance = mutableListOf(
                    AccountBalance(
                        id = sequence.nextId(),
                        accountBalance = BigDecimal(50000),
                        accountBalanceDate = Date(),
                        accountBalanceType = AccountBalanceType.AVAILABLE_BALANCE,
                    )
                ),
                referenceAccount = null,
            ),
            amount = BigDecimal(900000),
            dt = DateTime(),
            currency = CurrencyType.CNY,
            purpose = ""
        )


        Assertions.assertThat(account).isNotNull
    }

    @Test
    fun `add balance`(){
        val balances = mutableListOf<AccountBalance>()

        accountService.increaseBalance(
            balances = balances,
            amount = BigDecimal(5000),
            dt = DateTime(),
            accountBalanceType = AccountBalanceType.AVAILABLE_BALANCE,
        )

        Assertions.assertThat(balances.size).isEqualTo(1)
        Assertions.assertThat(balances[0].accountBalance).isEqualTo(BigDecimal(5000))
    }

    @Test
    fun `increase balance`(){
        val balances = mutableListOf(
            AccountBalance(
                id = sequence.nextId(),
                accountBalance = BigDecimal(50000),
                accountBalanceDate = Date(),
                accountBalanceType = AccountBalanceType.AVAILABLE_BALANCE,
            )
        )

        accountService.increaseBalance(
            balances = balances,
            amount = BigDecimal(5000),
            dt = DateTime(),
            accountBalanceType = AccountBalanceType.AVAILABLE_BALANCE,
        )

        Assertions.assertThat(balances.size).isEqualTo(1)
        Assertions.assertThat(balances[0].accountBalance).isEqualTo(BigDecimal(55000))
    }
}