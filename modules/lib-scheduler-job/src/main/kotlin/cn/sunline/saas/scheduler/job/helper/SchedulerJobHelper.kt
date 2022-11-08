package cn.sunline.saas.scheduler.job.helper

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.job.component.execute
import cn.sunline.saas.scheduler.job.component.failed
import cn.sunline.saas.scheduler.job.component.succeed
import cn.sunline.saas.scheduler.job.model.SchedulerJobLog
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import org.springframework.stereotype.Component

@Component
class SchedulerJobHelper(
    private val schedulerJobLogService: SchedulerJobLogService,
    private val tenantDateTime: TenantDateTime,
) {
    fun execute(jobId:String): SchedulerJobLog?{
        val schedulerJobLog = schedulerJobLogService.getOne(jobId.toLong())
        schedulerJobLog?.run {
            ContextUtil.setTenant(this.getTenantId().toString())
            this.execute(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }
        return schedulerJobLog
    }

    fun failed(schedulerJobLog: SchedulerJobLog?,log:String?){
        schedulerJobLog?.run {
            this.failed(tenantDateTime.now(),log)
            schedulerJobLogService.save(this)
        }
    }

    fun succeed(schedulerJobLog: SchedulerJobLog?){
        schedulerJobLog?.run {
            this.succeed(tenantDateTime.now())
        }
    }
}