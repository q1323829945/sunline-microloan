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
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LoanApplyStatisticsSchedulerTask(
    actorType: String = ActorType.LOAN_APPLY_STATISTICS.name,
    entityConfig: EntityConfig? = null
) : AbstractActor(actorType, entityConfig) {
    private var logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    @Autowired
    private lateinit var schedulerJobLogService: SchedulerJobLogService

    @Autowired
    private lateinit var loanApplyAppService: LoanApplyAppService


    override fun doJob(actorId: String, jobId: String, data: ActorCommand) {

        val schedulerJobLog = schedulerJobLogService.getOne(jobId.toLong())
        schedulerJobLog?.run {
            ContextUtil.setTenant(this.getTenantId().toString())
            this.execute(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }

        try {
            logger.info("[LoanApplyStatisticsSchedulerTask]: sync $actorId statistics start")
            loanApplyAppService.syncLoanApplicationStatistics(actorId)
            logger.info("[LoanApplyStatisticsSchedulerTask]: sync $actorId statistics end")
            schedulerJobLog?.run {
                this.succeed(tenantDateTime.now())
                schedulerJobLogService.save(this)
            }
            //delete reminder
            ActorReminderService.deleteReminders(actorType, actorId, jobId)
        } catch (e: Exception) {
            //do nothing,until pass
            logger.error("[LoanApplyStatisticsSchedulerTask]: sync applicationId:$actorId , error massage : ${e.message}")
        }
    }
}