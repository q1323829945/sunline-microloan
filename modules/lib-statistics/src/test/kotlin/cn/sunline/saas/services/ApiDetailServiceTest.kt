package cn.sunline.saas.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setTimeZone
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.dto.DTOApiDetailQueryParams
import cn.sunline.saas.statistics.services.ApiDetailService
import org.assertj.core.api.Assertions
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiDetailServiceTest(@Autowired val apiDetailService: ApiDetailService, @Autowired  val tenantDateTime: TenantDateTime) {
    @BeforeAll
    fun `init`(){
        ContextUtil.setTimeZone(DateTimeZone.UTC)
        ContextUtil.setTenant("12344566")
        apiDetailService.saveApiDetail("menus/")
    }

    @Test
    fun `get count`(){
        val startDate = tenantDateTime.now().plusDays(-1).toDate()
        val endDate = tenantDateTime.now().toDate()
        val count = apiDetailService.getGroupByApiCount(DTOApiDetailQueryParams(startDate,endDate))

        Assertions.assertThat(count[0].api).isEqualTo("menus/")
        Assertions.assertThat(count[0].count).isEqualTo(1L)
    }
}