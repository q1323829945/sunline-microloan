package cn.sunline.saas.formula

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.math.BigDecimal

/**
 * @title: CalculateEqualInstalmentTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/20 11:34
 */
class CalculateEqualInstalmentTest {

    @Test
    fun `test equal instalment formula`() {
        val actual = CalculateEqualInstalment.getInstalment(BigDecimal("1000000"), BigDecimal("6.3"), 12)

        Assertions.assertThat(actual).isEqualTo(BigDecimal("86204.38"))
    }

}