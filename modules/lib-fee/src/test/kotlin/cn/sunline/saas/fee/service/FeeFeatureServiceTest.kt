package cn.sunline.saas.fee.service

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.fee.model.dto.DTOFeeFeatureAdd
import cn.sunline.saas.global.constant.LoanFeeType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @title: FeeFeatureServiceTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/14 11:59
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FeeFeatureServiceTest(@Autowired private val feeFeatureService: FeeFeatureService) {

    @Test
    fun `entity save`() {
        val feeFeatures = mutableListOf<DTOFeeFeatureAdd>()
        val feeFeature1 = DTOFeeFeatureAdd(
            feeType = LoanFeeType.PREPAYMENT,
            feeMethodType = FeeMethodType.FIX_AMOUNT,
            feeAmount = BigDecimal(150),
            feeRate = null,
            feeDeductType = FeeDeductType.IMMEDIATE
        )
        val feeFeature2 = DTOFeeFeatureAdd(
            feeType = LoanFeeType.OVERDUE,
            feeMethodType = FeeMethodType.FEE_RATIO,
            feeAmount = null,
            feeRate = BigDecimal(1.5),
            feeDeductType = FeeDeductType.IMMEDIATE
        )

        feeFeatures.add(feeFeature1)
        feeFeatures.add(feeFeature2)

        val actual = feeFeatureService.register(1,feeFeatures)

        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual.size).isEqualTo(2)
    }
}