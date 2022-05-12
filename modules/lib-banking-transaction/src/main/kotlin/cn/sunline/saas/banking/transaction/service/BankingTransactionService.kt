package cn.sunline.saas.banking.transaction.service

import cn.sunline.saas.banking.transaction.model.db.BankingTransaction
import cn.sunline.saas.banking.transaction.model.dto.DTOBankingTransaction
import cn.sunline.saas.banking.transaction.repository.BankingTransactionRepository
import cn.sunline.saas.global.constant.TransactionStatus
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import cn.sunline.saas.seq.Sequence
import org.joda.time.Instant

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
    private lateinit var seq: Sequence

    fun initiate(dtoBankingTransaction: DTOBankingTransaction): BankingTransaction {
        val bankingTransaction = BankingTransaction(
            id = seq.nextId(),
            name = dtoBankingTransaction.name,
            agreementId = dtoBankingTransaction.agreementId,
            instructionId = dtoBankingTransaction.instructionId,
            initiatedDate = Instant.now(),
            executedDate = null,
            transactionDescription = dtoBankingTransaction.transactionDescription,
            transactionStatus = TransactionStatus.INITIATE,
            currency = dtoBankingTransaction.currency,
            amount = dtoBankingTransaction.amount,
            appliedFee = dtoBankingTransaction.appliedFee,
            appliedRate = dtoBankingTransaction.appliedRate,
            businessUnit = dtoBankingTransaction.businessUnit
        )
        return save(bankingTransaction)
    }

    fun execute(bankingTransaction:BankingTransaction):BankingTransaction{
        bankingTransaction.executedDate = Instant.now()
        bankingTransaction.transactionStatus = TransactionStatus.EXECUTED
        return save(bankingTransaction)
    }

}

