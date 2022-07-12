package cn.sunline.saas.service

import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstructionType
import cn.sunline.saas.repayment.instruction.model.dto.DTORepaymentInstructionAdd
import cn.sunline.saas.repayment.instruction.service.RepaymentInstructionService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RepaymentInstructionServiceTest {
    @Autowired
    private lateinit var repaymentInstructionService: RepaymentInstructionService

    var id = 0L

    @BeforeAll
    fun init(){
        ContextUtil.setTenant("123")

        val moneyTransferInstruction = repaymentInstructionService.registered(
            dtoRepaymentInstructionAdd = DTORepaymentInstructionAdd(
                moneyTransferInstructionAmount = BigDecimal(5000),
                moneyTransferInstructionCurrency = CurrencyType.CNY,
                moneyTransferInstructionPurpose = null,
                payeeAccount = null,
                payerAccount = null,
                agreementId = 1,
                businessUnit = 1,
                referenceId = 1,
                startDate = Date(),
                operator = null,
            )
        )

        Assertions.assertThat(moneyTransferInstruction).isNotNull

        id = moneyTransferInstruction.id
    }


    @Test
    fun retrieve(){
        val moneyTransferInstruction = repaymentInstructionService.retrieve(id)

        Assertions.assertThat(moneyTransferInstruction).isNotNull

        Assertions.assertThat(moneyTransferInstruction.instructionAmount).isEqualTo("5000.00")
    }

    @Test
    fun getPage(){
        val page = repaymentInstructionService.getPage(
            agreementId = null,
            customerId = null,
        moneyTransferInstructionType = MoneyTransferInstructionType.REPAYMENT,
        moneyTransferInstructionStatus = null,
        Pageable.unpaged())

        Assertions.assertThat(page.size).isEqualTo(1)
    }


    @Test
    fun getPageByInvoiceId(){
        val page = repaymentInstructionService.getPageByInvoiceId(1, Pageable.unpaged())

        Assertions.assertThat(page.size).isEqualTo(1)
    }

}