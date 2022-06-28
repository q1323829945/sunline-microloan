package cn.sunline.saas.services

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setTimeZone
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.dto.DTOApiStatistics
import cn.sunline.saas.statistics.modules.dto.DTOApiStatisticsFindParams
import cn.sunline.saas.statistics.services.ApiStatisticsService
import org.assertj.core.api.Assertions
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiStatisticsServiceTest(@Autowired val apiStatisticsService: ApiStatisticsService, @Autowired val tenantDateTime: TenantDateTime) {
    @BeforeAll
    fun `init`(){
        ContextUtil.setTimeZone(DateTimeZone.UTC)
        ContextUtil.setTenant("12344566")
        apiStatisticsService.saveApiStatistics(DTOApiStatistics("menus/",1,Frequency.D))
    }


    @Test
    fun `get one`(){
        val dateTime = tenantDateTime.now()
        val statistics = apiStatisticsService.findByDate(DTOApiStatisticsFindParams("menus/",dateTime,Frequency.D))

        Assertions.assertThat(statistics).isNotNull
        Assertions.assertThat(statistics?.count).isEqualTo(1)
        Assertions.assertThat(statistics?.api).isEqualTo("menus/")
        Assertions.assertThat(statistics?.frequency).isEqualTo(Frequency.D)
    }
}