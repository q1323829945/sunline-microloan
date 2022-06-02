package cn.sunline.saas.multi_tenant.util

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTimeZone
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.text.SimpleDateFormat

/**
 * @title: TenantDateTimeTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/24 10:55
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TenantDateTimeTest(@Autowired val tenantDateTime: TenantDateTime) {

    @BeforeAll
    fun `init`() {
        ContextUtil.setTimeZone(DateTimeZone.forID(CountryType.GBR.datetimeZone))
    }

    @Test
    fun `test tenant time zone to date`() {

        val now = DateTime.now()
        val tenantDateTime = now.toDate()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

        Assertions.assertThat(simpleDateFormat.format(tenantDateTime)).isEqualTo(now.toString())
    }

    @Test
    fun `test tenant time zone is local zone`() {

        val now = tenantDateTime.now()
        val tenantDateTime = now.toDate()
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")

        Assertions.assertThat(simpleDateFormat.format(tenantDateTime)).isNotEqualTo(now.toString())
    }

    @Test
    fun `test between two datetime`(){
        // now is 2022-05-25T23:23:40.123Z
        val dt1 = DateTime(2022, 5, 25, 23, 30, 40, 123, DateTimeZone.UTC)
        val dt2 = DateTime(2022, 5, 25, 23, 23, 40, 123, DateTimeZone.UTC)
        val actual1 = tenantDateTime.betweenTimes(dt1,dt2)

        Assertions.assertThat(actual1.minutes).isEqualTo(7)


        val dt3 = DateTime(2022, 5, 26, 23, 30, 40, 123, DateTimeZone.UTC)
        val dt4 = DateTime(2022, 5, 25, 23, 23, 40, 123, DateTimeZone.UTC)
        val actual2 = tenantDateTime.betweenTimes(dt3,dt4)

        Assertions.assertThat(actual2.minutes).isEqualTo(7)
        Assertions.assertThat(actual2.days * 24 + actual2.hours).isEqualTo(24)

    }

    @Test
    fun `test getYearMonthDay`(){
        val dt1 = DateTime(2022, 5, 25, 23, 30, 40, 123, DateTimeZone.UTC)
        val actual1 = tenantDateTime.getYearMonthDay(dt1)

        Assertions.assertThat(actual1).isEqualTo("20220525")
    }
}