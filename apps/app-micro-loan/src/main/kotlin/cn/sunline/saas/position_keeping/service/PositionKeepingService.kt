package cn.sunline.saas.position_keeping.service

import cn.sunline.saas.account.model.dto.DTOAccountAdd
import cn.sunline.saas.account.service.LoanAccountService
import cn.sunline.saas.banking.transaction.model.db.BankingTransaction
import cn.sunline.saas.banking.transaction.model.dto.DTOBankingTransaction
import cn.sunline.saas.banking.transaction.service.BankingTransactionService
import cn.sunline.saas.global.constant.TransactionStatus
import cn.sunline.saas.rpc.pubsub.PositionKeepingPublish
import cn.sunline.saas.rpc.pubsub.dto.DTOBusinessDetail
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: PositionKeepingService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/7 15:37
 */
@Service
class PositionKeepingService(
    private val positionKeepingPublish: PositionKeepingPublish
) {

    @Autowired
    private lateinit var bankingTransactionService: BankingTransactionService

    @Autowired
    private lateinit var loanAccountService: LoanAccountService

    fun registeredPositionKeeping(dtoBankingTransaction: DTOBankingTransaction) {
        val bankingTransaction = bankingTransactionService.initiate(dtoBankingTransaction)
        val dealAccount = runBlocking(CoroutineName("POSITION-KEEPING-ACCOUNT")) {
            entryPositionKeepingAccount(bankingTransaction)
        }
    }

    suspend fun entryPositionKeepingAccount(bankingTransaction: BankingTransaction) = coroutineScope {
        if (bankingTransaction.transactionStatus == TransactionStatus.INITIATE) {
            val dtoAccountAdd = bankingTransactionService.execute(bankingTransaction).run {
                DTOAccountAdd(
                    id = agreementId,
                    purpose = transactionDescription,
                    currency = currency,
                    amount = amount.toPlainString(),
                    date = executedDate!!,
                    businessUnit = businessUnit,
                    customerId = customerId
                )
            }

            loanAccountService.saveAccount(dtoAccountAdd)

            positionKeepingPublish.addBusinessDetail(DTOBusinessDetail(
                agreementId = bankingTransaction.agreementId,
                customerId = bankingTransaction.customerId,
                amount = bankingTransaction.amount,
                currency = bankingTransaction.currency
            ))
        }
    }

}