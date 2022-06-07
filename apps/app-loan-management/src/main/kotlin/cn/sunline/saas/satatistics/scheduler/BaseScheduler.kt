package cn.sunline.saas.satatistics.scheduler

import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.joda.time.DateTime
import org.springframework.data.domain.Pageable

open class BaseScheduler(
    private val tenantDateTime: TenantDateTime,
    private val tenantService: TenantService
) {
    fun getNowDate(): DateTime {
        return tenantDateTime.toTenantDateTime(tenantDateTime.now().toLocalDate().toDate())
    }

    fun getTenantIdList():List<Tenant>{
        return tenantService.getPaged(null, Pageable.unpaged()).content
    }
}