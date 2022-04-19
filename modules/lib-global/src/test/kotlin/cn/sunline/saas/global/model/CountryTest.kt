package cn.sunline.saas.global.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * @title: CountryTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/21 12:17
 */
class CountryTest {

    @Test
    fun `enum item`(){
        assertThat(CountryType.values().size).isEqualTo(16)

        val china = CountryType.CHN
        assertThat(china.countryCode).isEqualTo("CHN")
        assertThat(china.countryName).isEqualTo("China")
        assertThat(china.numberCode).isEqualTo("156")
        assertThat(china.currencyCode).isEqualTo("CNY")
        assertThat(china.mobileArea).isEqualTo("86")
        assertThat(china.datetimeZone).isEqualTo("Asia/Shanghai")
    }

}