package cn.sunline.saas.service

import cn.sunline.saas.disbursement.instruction.model.dto.DTODisbursementInstructionAdd
import cn.sunline.saas.disbursement.instruction.service.DisbursementInstructionService
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DisbursementInstructionServiceTest {
    @Autowired
    private lateinit var disbursementInstructionService: DisbursementInstructionService

    var id:Long = 0

    @BeforeAll
    fun init(){
        ContextUtil.setTenant("1")

        val disbursementInstruction = disbursementInstructionService.registered(
            dtoDisbursementInstruction = DTODisbursementInstructionAdd(
                moneyTransferInstructionAmount = BigDecimal(50000),
                moneyTransferInstructionCurrency = CurrencyType.CNY,
                moneyTransferInstructionPurpose = "",
                payeeAccount = null,
                payerAccount = null,
                agreementId = 1,
                businessUnit = 1,
            )
        )

        Assertions.assertThat(disbursementInstruction).isNotNull
        id = disbursementInstruction.id

    }


    @Test
    fun retrieve(){
        val disbursementInstruction = disbursementInstructionService.retrieve(id)


        Assertions.assertThat(disbursementInstruction).isNotNull
        Assertions.assertThat(disbursementInstruction.instructionAmount).isEqualTo("50000.00")
    }
}