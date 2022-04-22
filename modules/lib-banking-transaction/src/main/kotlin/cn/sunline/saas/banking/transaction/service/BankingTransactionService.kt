package cn.sunline.saas.banking.transaction.service

import cn.sunline.saas.banking.transaction.model.BankingTransaction
import cn.sunline.saas.banking.transaction.repository.AppliedInterestRepository
import cn.sunline.saas.banking.transaction.repository.BankingTransactionRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: BankingTransactionService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 13:48
 */
@Service
class BankingTransactionService(private val bankingTransactionRepo: BankingTransactionRepository) :
    BaseMultiTenantRepoService<BankingTransaction, Long>(bankingTransactionRepo) {

    @Autowired
    private lateinit var appliedInterestRepository: AppliedInterestRepository

    fun registered(){

    }
}