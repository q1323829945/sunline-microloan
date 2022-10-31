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
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging

class BusinessStatisticsSchedulerTask(
    private val tenantDateTime: TenantDateTime,
    private val schedulerJobLogService: SchedulerJobLogService,
    private val loanApplyAppService: LoanApplyAppService,
    actorType:String = ActorType.BUSINESS_STATISTICS.name,
    entityConfig: EntityConfig? = null
): AbstractActor(actorType, entityConfig) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    private var logger = KotlinLogging.logger {}

    override fun doJob(actorId: String, jobId: String, data: ActorCommand) {
        val schedulerJobLog = schedulerJobLogService.getOne(jobId.toLong())
        schedulerJobLog?.run {
            ContextUtil.setTenant(this.getTenantId().toString())
            this.execute(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }

        try {

            logger.info("[doJob]: save business $actorId statistic start")

            loanApplyAppService.saveBusinessStatistic(actorId)

            logger.info("[doJob]: save business $actorId statistic end")


        } catch (e: Exception) {
            //do nothing,until pass
            logger.error("[doJob]: save business applicationId:${actorId} statistic, error massage : ${e.message}")
            return
        }

        logger.info("[doJob]: save business  $actorId statistic Success")
        schedulerJobLog?.run {
            this.succeed(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }
        //delete reminder
        ActorReminderService.deleteReminders(actorType, actorId, jobId)
    }
}