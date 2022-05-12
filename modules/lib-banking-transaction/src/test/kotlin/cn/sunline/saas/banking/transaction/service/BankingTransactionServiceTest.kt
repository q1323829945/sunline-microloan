package cn.sunline.saas.banking.transaction.service

import cn.sunline.saas.banking.transaction.model.dto.DTOBankingTransaction
import cn.sunline.saas.global.constant.TransactionStatus
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

/**
 * @title: BankingTransactionServiceTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/7 16:11
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BankingTransactionServiceTest(@Autowired val bankingTransactionService: BankingTransactionService) {

    @BeforeAll
    fun `init`(){
        ContextUtil.setTenant("12344566")
    }

    @Test
    fun `save new banking transaction object`() {
        val dtoBankingTransaction = DTOBankingTransaction(
            name = "test",
            agreementId = 123245,
            instructionId = 12341455,
            transactionDescription = null,
            currency = CurrencyType.USD,
            amount = BigDecimal("123"),
            appliedFee = null,
            appliedRate = null,
            businessUnit = 50000,
            customerId = 1234567788
        )

        val bankingTransaction = bankingTransactionService.initiate(dtoBankingTransaction)

        assertThat(bankingTransaction).isNotNull
        assertThat(bankingTransaction.transactionStatus).isEqualTo(TransactionStatus.INITIATE)
    }

    @Test
    fun `update banking transaction set status = EXECUTED`(){
        val dtoBankingTransaction = DTOBankingTransaction(
            name = "test",
            agreementId = 123245,
            instructionId = 12341455,
            transactionDescription = null,
            currency = CurrencyType.USD,
            amount = BigDecimal("123"),
            appliedFee = null,
            appliedRate = null,
            businessUnit = 50000,
            customerId = 1234567788
        )

        val bankingTransaction = bankingTransactionService.initiate(dtoBankingTransaction)
        val bankingTransactionNew = bankingTransactionService.execute(bankingTransaction)

        assertThat(bankingTransactionNew.transactionStatus).isEqualTo(TransactionStatus.EXECUTED)
        assertThat(bankingTransactionNew.executedDate).isNotNull
    }
}