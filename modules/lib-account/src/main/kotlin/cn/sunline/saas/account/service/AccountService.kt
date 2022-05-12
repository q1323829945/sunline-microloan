package cn.sunline.saas.account.service

import cn.sunline.saas.account.model.*
import cn.sunline.saas.account.model.db.Account
import cn.sunline.saas.account.model.db.AccountBalance
import cn.sunline.saas.account.model.db.AccountEntry
import cn.sunline.saas.account.model.db.AccountInvolvement
import cn.sunline.saas.account.model.dto.DTOAccountAdd
import cn.sunline.saas.account.repository.AccountEntryRepository
import cn.sunline.saas.account.repository.AccountInvolvementRepository
import cn.sunline.saas.account.repository.AccountRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
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

    @Transactional
    fun saveAccount(
        accountClass: AccountClass, accountType: AccountType, accountBalanceType: AccountBalanceType,
        dtoAccountAdd: DTOAccountAdd
    ) {
        val account = accountRepository.findByIdOrNull(dtoAccountAdd.id)
        if (account == null) {
            createAccount(accountClass, accountType, accountBalanceType, dtoAccountAdd)
        } else {
            updateAccountBalance(accountBalanceType, account, dtoAccountAdd)
        }
    }

    private fun createAccount(
        accountClass: AccountClass,
        accountType: AccountType,
        accountBalanceType: AccountBalanceType,
        dtoAccountAdd: DTOAccountAdd
    ) {
        val accountBalances = mutableListOf<AccountBalance>()
        accountBalances.add(
            AccountBalance(
                id = seq.nextId(),
                accountBalance = BigDecimal(dtoAccountAdd.amount),
                accountBalanceDate = dtoAccountAdd.date,
                accountBalanceType = accountBalanceType
            )
        )

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

        accountRepository.save(account)
        accountInvolvementRepository.saveAll(accountInvolvements)

        saveAccountEntry(account.id, dtoAccountAdd)
    }

    private fun updateAccountBalance(
        accountBalanceType: AccountBalanceType,
        account: Account,
        dtoAccountAdd: DTOAccountAdd
    ) {
        account.accountBalance.first { it.accountBalanceType == accountBalanceType }.run {
            accountBalance = accountBalance.subtract(BigDecimal(dtoAccountAdd.amount))
            accountBalanceDate = dtoAccountAdd.date
        }

        accountRepository.save(account)
        saveAccountEntry(account.id, dtoAccountAdd)
    }

    private fun saveAccountEntry(accountId: Long, dtoAccountAdd: DTOAccountAdd) {
        val accountEntry = AccountEntry(
            id = seq.nextId(),
            accountId = accountId,
            accountEntryAmount = BigDecimal(dtoAccountAdd.amount),
            accountEntryCurrency = dtoAccountAdd.currency,
            accountPurpose = dtoAccountAdd.purpose,
            accountEntryValueDate = dtoAccountAdd.date
        )
        accountEntryRepository.save(accountEntry)
    }

}