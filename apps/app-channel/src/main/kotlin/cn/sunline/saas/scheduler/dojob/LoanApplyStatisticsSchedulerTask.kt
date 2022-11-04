package cn.sunline.saas.scheduler.dojob

import cn.sunline.saas.dapr_wrapper.actor.ActorCommand
import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.loan_apply.service.LoanApplyAppService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.job.component.execute
import cn.sunline.saas.scheduler.job.component.succeed
import cn.sunline.saas.scheduler.job.helper.SchedulerJobHelper
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import mu.KotlinLogging

class LoanApplyStatisticsSchedulerTask(
    private val tenantDateTime: TenantDateTime,
    private val schedulerJobHelper: SchedulerJobHelper,
    private val loanApplyAppService: LoanApplyAppService,
    actorType: String = ActorType.LOAN_APPLY_STATISTICS.name,
    entityConfig: EntityConfig? = null
) : AbstractActor(actorType, entityConfig) {
    private var logger = KotlinLogging.logger {}


    override fun doJob(actorId: String, jobId: String, data: ActorCommand) {
        val schedulerJobLog = schedulerJobHelper.execute(jobId)

        try {
            logger.info("[LoanApplyStatisticsSchedulerTask]: sync $actorId statistics start")
            loanApplyAppService.syncLoanApplicationStatistics(actorId, null)
            logger.info("[LoanApplyStatisticsSchedulerTask]: sync $actorId statistics end")
            schedulerJobHelper.succeed(schedulerJobLog)
            //delete reminder
            ActorReminderService.deleteReminders(actorType, actorId, jobId)
        } catch (e: Exception) {
            //do nothing,until pass
            logger.error("[LoanApplyStatisticsSchedulerTask]: sync applicationId:$actorId , error massage : ${e.message}")
        }
    }
}