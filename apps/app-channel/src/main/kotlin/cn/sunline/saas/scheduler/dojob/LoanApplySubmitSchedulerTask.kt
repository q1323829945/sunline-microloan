package cn.sunline.saas.scheduler.dojob

import cn.sunline.saas.dapr_wrapper.actor.ActorCommand
import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.job.component.execute
import cn.sunline.saas.scheduler.job.component.succeed
import cn.sunline.saas.scheduler.job.helper.SchedulerJobHelper
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import mu.KotlinLogging

class LoanApplySubmitSchedulerTask(
    private val tenantDateTime: TenantDateTime,
    private val schedulerJobHelper: SchedulerJobHelper,
    private val loanAgentService: LoanAgentService,
    actorType:String = ActorType.LOAN_APPLY_SUBMIT.name,
    entityConfig: EntityConfig? = null
): AbstractActor(actorType, entityConfig) {
    private var logger = KotlinLogging.logger {}


    override fun doJob(actorId: String, jobId: String, data: ActorCommand) {
        val schedulerJobLog = schedulerJobHelper.execute(jobId)

        try {
            //TODO: send to Ios
            logger.info("send $actorId to Ios")

        } catch (e:Exception){
            //do nothing,until pass
            logger.error("applicationId:$actorId , error massage : ${e.message}")
            return
        }

        logger.info("sen $actorId to Ios Success")
        schedulerJobHelper.succeed(schedulerJobLog)
        //delete reminder
        ActorReminderService.deleteReminders(actorType,actorId,jobId)

        loanAgentService.updateStatus(actorId.toLong(),ApplyStatus.SUBMIT)
    }
}