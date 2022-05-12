package cn.sunline.saas.account.service

import cn.sunline.saas.account.model.db.Account
import cn.sunline.saas.account.repository.AccountEntryRepository
import cn.sunline.saas.account.repository.AccountInvolvementRepository
import cn.sunline.saas.account.repository.AccountRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service

/**
 * @title: InterestAccountService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/10 14:29
 */
@Service
class InterestAccountService (
    private val accountRepository: AccountRepository,
    private val accountEntryRepository: AccountEntryRepository,
    private val accountInvolvementRepository: AccountInvolvementRepository
) : BaseMultiTenantRepoService<Account, Long>(accountRepository) {

}