package cn.sunline.saas.account.service

import cn.sunline.saas.account.model.AccountBalanceType
import cn.sunline.saas.account.model.AccountClass
import cn.sunline.saas.account.model.AccountType
import cn.sunline.saas.account.model.dto.DTOAccountAdd
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

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

    fun saveAccount(dtoAccountAdd: DTOAccountAdd) {
        accountService.saveAccount(
            AccountClass.LOAN,
            AccountType.DEBIT_ACCOUNT,
            AccountBalanceType.LOAN_OUTSTANDING_AMOUNT,
            dtoAccountAdd
        )
    }


}