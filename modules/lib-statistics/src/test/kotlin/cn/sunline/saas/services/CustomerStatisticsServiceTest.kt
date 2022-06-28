package cn.sunline.saas.services

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setTimeZone
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.dto.DTOCustomerStatistics
import cn.sunline.saas.statistics.modules.dto.DTOCustomerStatisticsFindParams
import cn.sunline.saas.statistics.services.CustomerStatisticsService
import org.assertj.core.api.Assertions
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerStatisticsServiceTest(@Autowired val customerStatisticsService: CustomerStatisticsService,@Autowired val tenantDateTime: TenantDateTime) {
    @BeforeAll
    fun `init`(){
        ContextUtil.setTimeZone(DateTimeZone.UTC)
        ContextUtil.setTenant("12344566")
        customerStatisticsService.saveCustomerStatistics(DTOCustomerStatistics(
            personCount = 2,
            organisationCount = 3,
            partyCount = 5,
            frequency = Frequency.D,
        ))
    }

    @Test
    fun `get statistics`(){
        val statistics = customerStatisticsService.findByDate(DTOCustomerStatisticsFindParams(tenantDateTime.now(),Frequency.D))


        Assertions.assertThat(statistics).isNotNull
        Assertions.assertThat(statistics?.partyCount).isEqualTo(5)
        Assertions.assertThat(statistics?.organisationCount).isEqualTo(3)
        Assertions.assertThat(statistics?.personCount).isEqualTo(2)
    }

}