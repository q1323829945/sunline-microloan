package cn.sunline.saas.global.model

import cn.sunline.saas.global.constant.LoanTermType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @title: LoanTermTypeTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/23 11:15
 */
class LoanTermTypeTest {

    @Test
    fun `test enum single`() {
        val values = LoanTermType.values()

        assertThat(values.size == 6).isTrue
        val single = values.single {
            it.term == TermType(12)
        }

        assertThat(single == LoanTermType.ONE_YEAR).isTrue
    }
}