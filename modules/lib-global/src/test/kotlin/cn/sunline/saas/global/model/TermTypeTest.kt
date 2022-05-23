package cn.sunline.saas.global.model

import cn.sunline.saas.global.constant.TermUnit
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

/**
 * @title: TermType
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/23 10:57
 */
class TermTypeTest {

    @Test
    fun `equals test`(){
        val term1 = TermType(16)
        val term2 = TermType(16,TermUnit.MONTH)

        Assertions.assertThat(term1 == term2).isTrue

    }

    @Test
    fun `test num is equaled and unit is not equaled`(){
        val term3 = TermType(1)
        val term4 = TermType(1,TermUnit.YEAR)

        Assertions.assertThat(term3 == term4).isFalse
    }
}