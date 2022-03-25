package cn.sunline.saas.util

import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.fee.exception.FeeConfigException
import cn.sunline.saas.fee.util.FeeUtil
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * @title: FeeUtilTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/25 10:57
 */
class FeeUtilTest {

    @Test
    fun `calculate fee amount`() {
        val actual1 = FeeUtil.calFeeAmount(BigDecimal("100"), BigDecimal("50"))
        assertThat(actual1).isEqualTo(BigDecimal("50"))

        val actual2 = FeeUtil.calFeeAmount(BigDecimal("100"), BigDecimal("2.5"))
        assertThat(actual2).isEqualTo(BigDecimal("2.5"))
    }

    @Test
    fun `validate fee's configuration data failed`() {
        assertThatThrownBy { FeeUtil.validFeeConfig(FeeMethodType.FEE_RATIO, null, null) }.isInstanceOf(
            FeeConfigException::class.java
        ).hasMessage("Fee calculation method config error")
    }

    @Test
    fun `validate fee's configuration data failed when ratio`() {
        assertThatThrownBy { FeeUtil.validFeeConfig(FeeMethodType.FEE_RATIO, BigDecimal("100"), null) }.isInstanceOf(
            FeeConfigException::class.java
        ).hasMessage("Fee calculation method config error")
    }

    @Test
    fun `validate fee's configuration data failed when fixed`() {
        assertThatThrownBy { FeeUtil.validFeeConfig(FeeMethodType.FIX_AMOUNT, null, BigDecimal("2.5")) }.isInstanceOf(
            FeeConfigException::class.java
        ).hasMessage("Fee calculation method config error")
    }
}