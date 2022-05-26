package cn.sunline.saas.scheduler.job.component

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.job.model.SchedulerTimer
import cn.sunline.saas.scheduler.job.service.SchedulerTimerService
import org.assertj.core.api.Assertions
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

/**
 * @title: CalculateSchedulerTimerTest
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 14:18
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CalculateSchedulerTimerTest(@Autowired val calculateSchedulerTimer: CalculateSchedulerTimer) {
    @Autowired
    private lateinit var schedulerTimerService: SchedulerTimerService

    @BeforeAll
    fun init() {
        val tenantId: Long = 100
        ContextUtil.setTenant(tenantId.toString())
        schedulerTimerService.save(SchedulerTimer(tenantId, 22))
    }

    @Test
    fun `hour of now is after hour of switch day time`() {
        // now is 2022-05-25T23:23:40.123Z
        val now = DateTime(2022, 5, 25, 23, 23, 40, 123, DateTimeZone.UTC)
        val actual = calculateSchedulerTimer.baseDateTime(now)

        Assertions.assertThat(actual.toString()).isEqualTo("2022-05-26T22:00:00.000Z")

    }

}