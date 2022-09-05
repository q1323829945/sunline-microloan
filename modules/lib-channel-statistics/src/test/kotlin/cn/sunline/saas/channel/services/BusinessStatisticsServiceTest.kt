package cn.sunline.saas.channel.services

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setTimeZone
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.statistics.modules.dto.DTOBusinessStatistics
import cn.sunline.saas.channel.statistics.modules.dto.DTOBusinessStatisticsFindParams
import cn.sunline.saas.channel.statistics.services.BusinessStatisticsService
import org.assertj.core.api.Assertions
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BusinessStatisticsServiceTest(@Autowired val businessStatisticsService: BusinessStatisticsService,@Autowired val tenantDateTime: TenantDateTime) {

    @BeforeAll
    fun `init`(){
        ContextUtil.setTimeZone(DateTimeZone.UTC)
        ContextUtil.setTenant("12344566")
        businessStatisticsService.saveBusinessStatistics(DTOBusinessStatistics(
            customerId = 1,
            paymentAmount = BigDecimal(10000),
            repaymentAmount = BigDecimal(500),
            currencyType = CurrencyType.CNY,
            frequency = Frequency.M,
        ))
    }

    @Test
    fun `get statistics`(){
        val statistics = businessStatisticsService.findByDate(DTOBusinessStatisticsFindParams(
            customerId = 1,
            datetime = tenantDateTime.now(),
            frequency = Frequency.M,
            currencyType = CurrencyType.CNY,
        ))


        Assertions.assertThat(statistics).isNotNull
        Assertions.assertThat(statistics?.paymentAmount).isEqualTo(BigDecimal(10000).setScale(2))
        Assertions.assertThat(statistics?.repaymentAmount).isEqualTo(BigDecimal(500).setScale(2))

    }
}