package cn.sunline.saas.scheduler.dojob

import cn.sunline.saas.dapr_wrapper.actor.ActorCommand
import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.job.helper.SchedulerJobHelper
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.dto.DTOActivityStepChange
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepChange
import cn.sunline.saas.workflow.step.modules.dto.DTOProcessStepChange
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService
import java.util.Date

class FinishEventHandleSchedulerTask (
    private val processStepService: ProcessStepService,
    private val activityStepService: ActivityStepService,
    private val eventStepService: EventStepService,
    private val tenantDateTime: TenantDateTime,
    private val schedulerJobHelper: SchedulerJobHelper,
    actorType:String = ActorType.FINISH_EVENT_HANDLE.name,
    entityConfig: EntityConfig? = null
): AbstractActor(actorType, entityConfig) {
    override fun doJob(actorId: String, jobId: String, data: ActorCommand) {
        val schedulerJobLog = schedulerJobHelper.execute(jobId)

        val process = processStepService.getOne(actorId.toLong())?: run {
            schedulerJobHelper.failed(schedulerJobLog,"process $jobId lose!")
            ActorReminderService.deleteReminders(actorType, actorId, jobId)
            return
        }

        process.activities.forEach { activity ->
            var status = StepStatus.FAILED
            var end:Date? = null
            activity.events.forEach {
                if(it.status == StepStatus.REJECTED){
                    status = StepStatus.REJECTED
                }
                if(it.status != StepStatus.PASSED && it.status != StepStatus.REJECTED){
                    eventStepService.updateOne(it.id, DTOEventStepChange(status = StepStatus.FAILED))
                }
            }
            if(activity.status != StepStatus.PASSED){
                if(status == StepStatus.REJECTED){
                    end = tenantDateTime.now().toDate()
                }
                activityStepService.updateOne(activity.id, DTOActivityStepChange(status = status, end = end))
            }
        }
        processStepService.updateOne(process.id, DTOProcessStepChange(status = StepStatus.REJECTED, end = tenantDateTime.now().toDate()))

        schedulerJobHelper.succeed(schedulerJobLog)
        ActorReminderService.deleteReminders(actorType, actorId, jobId)
    }

}