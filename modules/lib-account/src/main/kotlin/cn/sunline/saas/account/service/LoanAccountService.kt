package cn.sunline.saas.account.service

import cn.sunline.saas.account.component.reduce
import cn.sunline.saas.account.exception.AccountNotFoundException
import cn.sunline.saas.account.model.AccountBalanceType
import cn.sunline.saas.account.model.AccountClass
import cn.sunline.saas.account.model.AccountType
import cn.sunline.saas.account.model.db.Account
import cn.sunline.saas.account.model.db.AccountBalance
import cn.sunline.saas.account.model.dto.DTOAccountAdd
import cn.sunline.saas.account.model.dto.DTOAccountBalanceChange
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal

/**
 * @title: LoanAccountService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/10 11:07
 */
@Service
class LoanAccountService {

    @Autowired
    private lateinit var accountService: AccountService

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    fun initialAccount(dtoAccountAdd: DTOAccountAdd): Account {
        return accountService.initialAccount(
            AccountClass.LOAN,
            AccountType.DEBIT_ACCOUNT,
            AccountBalanceType.LOAN_OUTSTANDING_AMOUNT,
            dtoAccountAdd
        )
    }

    fun repayAccount(dtoAccountChange: DTOAccountBalanceChange): Account {
        val account = accountService.getOne(dtoAccountChange.id) ?: throw AccountNotFoundException("account not found")
        val dt = tenantDateTime.toTenantDateTime(dtoAccountChange.date)
        account.accountBalance.forEach {
            var amount = BigDecimal(dtoAccountChange.amount)
            if (it.accountBalanceType == AccountBalanceType.OVERDUE_PRINCIPAL) {
                amount = if (amount < it.accountBalance) amount else it.accountBalance
            }
            it.reduce(amount, dt)
        }

        return accountService.saveAccountBalance(
            account,
            BigDecimal(dtoAccountChange.amount),
            dt,
            dtoAccountChange.currency,
            dtoAccountChange.purpose
        )
    }

    fun overduePrincipal(dtoAccountChange: DTOAccountBalanceChange): Account {
        val account = accountService.getOne(dtoAccountChange.id) ?: throw AccountNotFoundException("account not found")
        val dt = tenantDateTime.toTenantDateTime(dtoAccountChange.date)
        accountService.increaseBalance(
            account.accountBalance,
            BigDecimal(dtoAccountChange.amount),
            dt,
            AccountBalanceType.OVERDUE_PRINCIPAL
        )
        return accountService.saveAccountBalance(
            account,
            BigDecimal(dtoAccountChange.amount),
            dt,
            dtoAccountChange.currency,
            dtoAccountChange.purpose
        )

    }


}