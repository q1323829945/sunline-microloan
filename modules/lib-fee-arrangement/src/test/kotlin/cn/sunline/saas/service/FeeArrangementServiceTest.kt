package cn.sunline.saas.service

import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementAdd
import cn.sunline.saas.fee.arrangement.service.FeeArrangementService
import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeeArrangementServiceTest {

    @Autowired
    private lateinit var feeArrangementService: FeeArrangementService

    var id = 0L

    val agreementId = 1L
    @BeforeAll
    fun init() {
        ContextUtil.setTenant("1")

        val feeArrangements = feeArrangementService.registered(
            agreementId = agreementId,
            dtoFeeArrangements = mutableListOf(
                DTOFeeArrangementAdd(
                    feeType = "",
                    feeMethodType = FeeMethodType.FEE_RATIO,
                    feeAmount = BigDecimal(500),
                    feeRate = BigDecimal(1.5),
                    feeDeductType = FeeDeductType.IMMEDIATE,
                ),
                DTOFeeArrangementAdd(
                    feeType = "",
                    feeMethodType = FeeMethodType.FEE_RATIO,
                    feeAmount = BigDecimal(600),
                    feeRate = BigDecimal(1.5),
                    feeDeductType = FeeDeductType.IMMEDIATE,
                )
            )
        )

        feeArrangements.forEach {
            id = it.id
        }

        Assertions.assertThat(feeArrangements.size).isEqualTo(2)
    }

    @Test
    fun `get fee arrangement list`(){
        val feeArrangements = feeArrangementService.listByAgreementId(agreementId)


        Assertions.assertThat(feeArrangements.size).isEqualTo(2)
    }
}