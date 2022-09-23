package cn.sunline.saas.scheduler.dojob

import cn.sunline.saas.dapr_wrapper.actor.ActorCommand
import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.loan_apply.service.LoanApplyAppService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.job.component.execute
import cn.sunline.saas.scheduler.job.component.succeed
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BusinessStatisticsSchedulerTask(
    actorType:String = ActorType.CHANNEL_STATISTICS.name,
    entityConfig: EntityConfig? = null
): AbstractActor(actorType, entityConfig) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
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