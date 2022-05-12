package cn.sunline.saas.account.repository

import cn.sunline.saas.account.model.db.AccountEntry
import cn.sunline.saas.base_jpa.repositories.BaseRepository

/**
 * @title: AccountEntryRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/10 11:05
 */
interface AccountEntryRepository : BaseRepository<AccountEntry, Long>