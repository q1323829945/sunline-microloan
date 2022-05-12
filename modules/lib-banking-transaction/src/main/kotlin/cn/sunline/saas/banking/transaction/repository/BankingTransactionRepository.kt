package cn.sunline.saas.banking.transaction.repository

import cn.sunline.saas.banking.transaction.model.db.BankingTransaction
import cn.sunline.saas.base_jpa.repositories.BaseRepository

/**
 * @title: BankingTransactionRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 13:47
 */
interface BankingTransactionRepository : BaseRepository<BankingTransaction, Long>