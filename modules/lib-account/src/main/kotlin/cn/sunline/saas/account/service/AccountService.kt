package cn.sunline.saas.account.service

import cn.sunline.saas.account.component.increase
import cn.sunline.saas.account.model.*
import cn.sunline.saas.account.model.db.Account
import cn.sunline.saas.account.model.db.AccountBalance
import cn.sunline.saas.account.model.db.AccountEntry
import cn.sunline.saas.account.model.db.AccountInvolvement
import cn.sunline.saas.account.model.dto.DTOAccountAdd
import cn.sunline.saas.account.repository.AccountEntryRepository
import cn.sunline.saas.account.repository.AccountInvolvementRepository
import cn.sunline.saas.account.repository.AccountRepository
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.transaction.Transactional

/**
 * @title: AccountService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/10 15:39
 */
@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val accountEntryRepository: AccountEntryRepository,
    private val accountInvolvementRepository: AccountInvolvementRepository
) : BaseMultiTenantRepoService<Account, Long>(accountRepository) {

    @Autowired
    private lateinit var seq: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    @Transactional
    fun initialAccount(
        accountClass: AccountClass,
        accountType: AccountType,
        accountBalanceType: AccountBalanceType,
        dtoAccountAdd: DTOAccountAdd
    ): Account {
        return accountRepository.findByIdOrNull(dtoAccountAdd.id) ?: createAccount(
            accountClass, accountType, accountBalanceType, dtoAccountAdd
        )
    }

    @Transactional
    fun saveAccountBalance(
        account: Account, amount: BigDecimal, dt: DateTime, currency: CurrencyType, purpose: String?
    ): Account {
        val accountPo = save(account)
        saveAccountEntry(account.id, amount, dt, currency, purpose)
        return accountPo
    }

    fun increaseBalance(
        balances: MutableList<AccountBalance>, amount: BigDecimal, dt: DateTime, accountBalanceType: AccountBalanceType
    ) {
        var exists = false
        balances.forEach {
            if (it.accountBalanceType == accountBalanceType){
                it.increase(amount,dt)
                exists = true
            }
        }
        if (!exists){
            balances.add(
                AccountBalance(
                    id = seq.nextId(),
                    accountBalance = amount,
                    accountBalanceDate = dt.toDate(),
                    accountBalanceType = accountBalanceType
                )
            )
        }
    }

    private fun createAccount(
        accountClass: AccountClass,
        accountType: AccountType,
        accountBalanceType: AccountBalanceType,
        dtoAccountAdd: DTOAccountAdd
    ): Account {
        val accountBalances = mutableListOf<AccountBalance>()
        val balance = BigDecimal(dtoAccountAdd.amount)
        val dt = tenantDateTime.toTenantDateTime(dtoAccountAdd.date)

        increaseBalance(accountBalances,balance, dt, accountBalanceType)

        val account = Account(
            id = dtoAccountAdd.id,
            accountStatus = AccountStatus.ACTIVATED,
            accountType = accountType,
            accountClass = accountClass,
            accountPurpose = dtoAccountAdd.purpose,
            accountCurrency = dtoAccountAdd.currency,
            accountBalance = accountBalances,
            referenceAccount = null
        )

        val accountInvolvements = mutableListOf<AccountInvolvement>()
        accountInvolvements.add(
            AccountInvolvement(
                id = seq.nextId(),
                accountId = account.id,
                accountInvolvement = dtoAccountAdd.customerId,
                AccountInvolvementType.ACCOUNT_OWNER
            )
        )
        accountInvolvements.add(
            AccountInvolvement(
                id = seq.nextId(),
                accountId = account.id,
                accountInvolvement = dtoAccountAdd.businessUnit,
                AccountInvolvementType.ACCOUNT_SERVICER
            )
        )

        val accountPo = accountRepository.save(account)
        accountInvolvementRepository.saveAll(accountInvolvements)

        saveAccountEntry(account.id, balance, dt, dtoAccountAdd.currency, dtoAccountAdd.purpose)
        return accountPo
    }

    private fun saveAccountEntry(
        accountId: Long, amount: BigDecimal, dt: DateTime, currency: CurrencyType, purpose: String?
    ) {
        val accountEntry = AccountEntry(
            id = seq.nextId(),
            accountId = accountId,
            accountEntryAmount = amount,
            accountEntryCurrency = currency,
            accountPurpose = purpose,
            accountEntryValueDate = dt.toDate()
        )
        accountEntryRepository.save(accountEntry)
    }

}