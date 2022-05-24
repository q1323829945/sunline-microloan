package cn.sunline.saas.global.util

import org.assertj.core.api.Assertions
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.Test

/**
 * @title: ContextUtilTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/24 15:23
 */
class ContextUtilTest {

    @Test
    fun `test set string value`(){
        val actual = "123455"
        ContextUtil.setTenant(actual)
        Assertions.assertThat(ContextUtil.getTenant()).isEqualTo(actual)

    }

    @Test
    fun `test time zone type`(){
        val actual = DateTimeZone.UTC

        ContextUtil.setTimeZone(actual)
        Assertions.assertThat(ContextUtil.getTimeZone()).isEqualTo(actual)
    }
}