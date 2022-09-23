package cn.sunline.saas.scheduler.dojob

import cn.sunline.saas.dapr_wrapper.actor.ActorCommand
import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.job.component.execute
import cn.sunline.saas.scheduler.job.component.succeed
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class LoanApplySubmitSchedulerTask(
    actorType:String = ActorType.LOAN_APPLY_SUBMIT.name,
    entityConfig: EntityConfig? = null
): AbstractActor(actorType, entityConfig) {
    private var logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    @Autowired
    private lateinit var schedulerJobLogService: SchedulerJobLogService

    @Autowired
    private lateinit var loanAgentService: LoanAgentService


    override fun doJob(actorId: String, jobId: String, data: ActorCommand) {
        val schedulerJobLog = schedulerJobLogService.getOne(jobId.toLong())
        schedulerJobLog?.run {
            this.execute(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }

        try {
            //TODO: send to Ios
            logger.info("send $actorId to Ios")

        } catch (e:Exception){
            //do nothing,until pass
            logger.error("applicationId:$actorId , error massage : ${e.message}")
            return
        }

        logger.info("sen $actorId to Ios Success")
        schedulerJobLog?.run {
            this.succeed(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }
        //delete reminder
        ActorReminderService.deleteReminders(actorType,actorId,jobId)

        loanAgentService.updateStatus(actorId.toLong(),ApplyStatus.SUBMIT)
    }
}