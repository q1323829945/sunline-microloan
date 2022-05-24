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
}