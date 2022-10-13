package cn.sunline.saas.interest.arrangement.component

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.arrangement.model.db.InterestArrangement
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.interest.model.InterestRate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * @title: InterestArrangementComponentTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/23 20:06
 */
class InterestArrangementComponentTest {

//    @Test
//    fun `getExecutionRate when interestType is fixed`() {
//        val interestArrangement = InterestArrangement(
//            id = 1,
//            interestType = InterestType.FIXED,
//            rate = BigDecimal("7.5"),
//            baseYearDays = BaseYearDays.ACTUAL_YEAR,
//            adjustFrequency = "1",
//            overdueInterestRatePercentage = BigDecimal("150"),
//            baseRate = BigDecimal("2.1")
//        )
//        val actual = interestArrangement.getExecutionRate()
//
//        assertThat(actual).isEqualTo(BigDecimal("7.5"))
//    }

//    @Test
//    fun `getExecutionRate when interestType is floating`() {
//        val interestArrangement = InterestArrangement(
//            id = 1,
//            interestType = InterestType.FLOATING_RATE_NOTE,
//            rate = BigDecimal("30"),
//            baseYearDays = BaseYearDays.ACTUAL_YEAR,
//            adjustFrequency = "1",
//            overdueInterestRatePercentage = BigDecimal("150"),
//            baseRate = BigDecimal("2.5")
//        )
//
//        val actual = interestArrangement.getExecutionRate()
//
//        assertThat(actual).isEqualTo(BigDecimal("0.032500"))
//    }
}