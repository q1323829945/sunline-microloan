package cn.sunline.saas.interest.util

import cn.sunline.saas.interest.constant.BaseYearDays
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * @title: InterestRateUtil
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/21 20:19
 */
class InterestRateUtilTest {

    @Test
    fun `to day rate is calculated by account year`() {
        val actual = InterestRateUtil.toDayRate(BaseYearDays.ACCOUNT_YEAR, BigDecimal("7.2"))
        assertThat(actual).isEqualTo(BigDecimal("0.020000"))
    }


    @Test
    fun `to day rate is calculated by actual year`() {
        val actual = InterestRateUtil.toDayRate(BaseYearDays.ACTUAL_YEAR, BigDecimal("7.2"))
        assertThat(actual).isEqualTo(BigDecimal("0.019726"))

    }
}