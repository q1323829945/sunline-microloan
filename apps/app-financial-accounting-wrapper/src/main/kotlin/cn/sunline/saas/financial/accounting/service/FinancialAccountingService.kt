package cn.sunline.saas.financial.accounting.service

import cn.sunline.saas.financial.accounting.model.FinancialAccountingTransaction
import cn.sunline.saas.financial.accounting.repository.FinancialAccountingTransactionRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.stereotype.Service

/**
 * @title: FinancialAccountingService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/22 11:08
 */
@Service
class FinancialAccountingService(private val financialAccountingTransactionRepository: FinancialAccountingTransactionRepository) :
    BaseMultiTenantRepoService<FinancialAccountingTransaction, Long>(financialAccountingTransactionRepository) {

        fun accounting(){
            //TODO save FinancialAccountingTransaction

            //TODO CALL partner's financial accounting application

        }
}