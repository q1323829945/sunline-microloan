package cn.sunline.saas.repayment.service

import cn.sunline.saas.global.constant.*
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.repayment.model.dto.DTOPrepaymentFeatureModalityAdd
import cn.sunline.saas.repayment.model.dto.DTORepaymentFeatureAdd
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

/**
 * @title: RepaymentProductFeatureServiceTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/9 13:55
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RepaymentProductFeatureServiceTest(
    @Autowired val repaymentProductFeatureService: RepaymentFeatureService
) {

    @BeforeAll
    fun init() {
        ContextUtil.setTenant("123")
    }

    @Test
    fun `entity save`() {
        val prepayments = mutableListOf <DTOPrepaymentFeatureModalityAdd>()
        val prepayment1  = DTOPrepaymentFeatureModalityAdd(
            LoanTermType.ONE_MONTH,
            PrepaymentType.NOT_ALLOWED,
            "0"
        )

//        val prepayment2  = DTOPrepaymentFeatureModalityAdd(
//            LoanTermType.THREE_MONTHS,
//            PrepaymentType.PARTIAL_PREPAYMENT,
//            "1.5"
//        )

        val prepayment3  = DTOPrepaymentFeatureModalityAdd(
            LoanTermType.SIX_MONTHS,
            PrepaymentType.FULL_REDEMPTION,
            "0"
        )

        prepayments.add(prepayment1)
//        prepayments.add(prepayment2)
        prepayments.add(prepayment3)

        val repaymentFeature = DTORepaymentFeatureAdd(
            PaymentMethodType.ONE_OFF_REPAYMENT,
            RepaymentFrequency.ONE_MONTH,
            RepaymentDayType.BASE_LOAN_DAY,
            prepayments,
            0
        )

        val actual = repaymentProductFeatureService.register(1,repaymentFeature)

        assertThat(actual).isNotNull
        assertThat(actual.payment).isNotNull
        assertThat(actual.prepayment.size).isEqualTo(2)
        assertThat(actual.prepayment[0].penaltyRatio).isEqualTo(BigDecimal.ZERO)
        assertThat(actual.prepayment[1].penaltyRatio).isEqualTo(BigDecimal(0))
        assertThat(actual.id).isNotNull
    }

}