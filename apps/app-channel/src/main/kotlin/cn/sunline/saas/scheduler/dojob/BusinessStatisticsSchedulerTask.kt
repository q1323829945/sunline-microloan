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
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging

class BusinessStatisticsSchedulerTask(
    private val tenantDateTime: TenantDateTime,
    private val schedulerJobHelper: SchedulerJobHelper,
    private val loanApplyAppService: LoanApplyAppService,
    actorType:String = ActorType.BUSINESS_STATISTICS.name,
    entityConfig: EntityConfig? = null
): AbstractActor(actorType, entityConfig) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    private var logger = KotlinLogging.logger {}

    override fun doJob(actorId: String, jobId: String, data: ActorCommand) {
        val schedulerJobLog = schedulerJobHelper.execute(jobId)

        if (schedulerJobLog?.schedulerTime!!.before(tenantDateTime.now().plusDays(1).plusMinutes(-2).toDate())) {
            schedulerJobHelper.failed(schedulerJobLog, "time out") //TODO error_content length no enough
            ActorReminderService.deleteReminders(actorType, actorId, jobId)
            return
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
        schedulerJobHelper.succeed(schedulerJobLog)
        //delete reminder
        ActorReminderService.deleteReminders(actorType, actorId, jobId)
    }
}