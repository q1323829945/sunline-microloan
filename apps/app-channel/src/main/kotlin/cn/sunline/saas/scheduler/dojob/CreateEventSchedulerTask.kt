package cn.sunline.saas.scheduler.dojob

import cn.sunline.saas.dapr_wrapper.actor.ActorCommand
import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.dapr_wrapper.actor.payload
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.create.CreateScheduler
import cn.sunline.saas.scheduler.dojob.dto.DTOCreateEventScheduler
import cn.sunline.saas.scheduler.dojob.dto.DTOSetEventUserScheduler
import cn.sunline.saas.scheduler.job.helper.SchedulerJobHelper
import cn.sunline.saas.workflow.defintion.modules.DefinitionStatus
import cn.sunline.saas.workflow.defintion.modules.db.ProcessDefinition
import cn.sunline.saas.workflow.defintion.services.ActivityDefinitionService
import cn.sunline.saas.workflow.defintion.services.EventDefinitionService
import cn.sunline.saas.workflow.defintion.services.ProcessDefinitionService
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.db.ActivityStep
import cn.sunline.saas.workflow.step.modules.db.EventStep
import cn.sunline.saas.workflow.step.modules.db.ProcessStep
import cn.sunline.saas.workflow.step.modules.dto.*
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepDataService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import mu.KotlinLogging
import org.springframework.data.domain.Pageable

class CreateEventSchedulerTask (
    private val processDefinitionService: ProcessDefinitionService,
    private val processStepService: ProcessStepService,
    private val activityDefinitionService: ActivityDefinitionService,
    private val activityStepService: ActivityStepService,
    private val eventDefinitionService: EventDefinitionService,
    private val eventStepService: EventStepService,
    private val eventStepDataService: EventStepDataService,
    private val schedulerJobHelper: SchedulerJobHelper,
    private val createScheduler: CreateScheduler,
    actorType:String = ActorType.CREATE_EVENT.name,
    entityConfig: EntityConfig? = null
): AbstractActor(actorType, entityConfig) {

    private val logger = KotlinLogging.logger {  }
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    override fun doJob(actorId: String, jobId: String, data: ActorCommand) {
        val schedulerJobLog = schedulerJobHelper.execute(jobId)

        val payload = data.payload<DTOCreateEventScheduler>()?: run {
            logger.error { "data error" }
            schedulerJobHelper.failed(schedulerJobLog,"data error")
            ActorReminderService.deleteReminders(actorType, actorId, jobId)
            return
        }
        val processDefinition = checkAndGetProcess()?: run {
            schedulerJobHelper.failed(schedulerJobLog,"process definition error !!!")
            createScheduler.create(ActorType.LOAN_APPLY_HANDLE, payload.applicationId.toString())
            ActorReminderService.deleteReminders(actorType, actorId, jobId)
            return
        }
        val processStep = createProcessStep(processDefinition)

        val startActivityStep = activityStepStart(processStep.id)!!
        val startEventStep = eventStepStart(startActivityStep.id,payload.applicationId.toLong(),payload.body)!!

        //set user
        createScheduler.create(
            ActorType.SET_EVENT_USER,
            processStep.id.toString(),
            DTOSetEventUserScheduler(
                applicationId = payload.applicationId,
                eventStepId = startEventStep.id.toString(),
                position = startActivityStep.activityDefinition.position,
                isCurrentEventStep = true
            )
        )
    }

    private fun activityStepStart(processStepId: Long):ActivityStep?{
        return activityStepService.getPaged(processStepId, Pageable.unpaged()).firstOrNull()?.run {
            activityStepService.updateOne(
                this.id,
                DTOActivityStepChange(
                    status = StepStatus.START,
                )
            )
        }
    }

    private fun eventStepStart(activityStepId: Long,applicationId:Long,data:Any? = null):EventStep?{
        return eventStepService.getPaged(activityStepId,Pageable.unpaged()).firstOrNull()?.run {
            val eventStep = eventStepService.updateOne(
                this.id,
                DTOEventStepChange(
                    status = StepStatus.START
                )
            )

            eventStepDataService.addData(DTOEventStepData(this.id,applicationId,objectMapper.writeValueAsString(data)))

            eventStep
        }
    }

    private fun checkAndGetProcess():ProcessDefinition?{
        val processDefinition = getProcessDefinition()?: run {
            logger.error { "Active process definition is not found !!!" }
            return null
        }
        val activity = activityDefinitionService.findPagedByProcess(processDefinition.id, Pageable.unpaged()).firstOrNull()?: run {
            logger.error { "First activity is not found !!" }
            return null
        }
        eventDefinitionService.findPagedByActivity(activity.id, Pageable.unpaged()).firstOrNull()?: run {
            logger.error { "First event is not found !!" }
            return null
        }
        return processDefinition
    }

    private fun createProcessStep(processDefinition: ProcessDefinition):ProcessStep{
        val processStep = processStepService.addOne(
            DTOProcessStepAdd(
                processDefinition.id
            )
        )
        createActivityStep(processStep.id,processDefinition.id)
        return processStep
    }



    private fun createActivityStep(processStepId:Long,processDefinitionId:Long){
        val activityDefinitions = activityDefinitionService.findPagedByProcess(processDefinitionId, Pageable.unpaged())
        var nextActivityId:Long? = null
        activityDefinitions.reversed().forEach { activityDefinition ->
            val activityStep = activityStepService.addOne(
                DTOActivityStepAdd(
                    processStepId,
                    activityDefinition.id,
                    next = nextActivityId
                )
            )
            nextActivityId = activityStep.id
            createEventStep(activityStep.id,activityDefinition.id)
        }
    }



    private fun createEventStep(activityStepId:Long,activityDefinitionId:Long){
        val eventDefinitions = eventDefinitionService.findPagedByActivity(activityDefinitionId, Pageable.unpaged())
        var nextEventId:Long? = null
        eventDefinitions.reversed().forEach { eventDefinition ->
            val eventStep = eventStepService.addOne(
                DTOEventStepAdd(
                    activityStepId,
                    eventDefinition.id,
                    nextEventId
                )
            )
            nextEventId = eventStep.id
        }
    }

    private fun getProcessDefinition():ProcessDefinition?{
        return processDefinitionService.getProcessPaged(
            DefinitionStatus.ACTIVE, Pageable.unpaged()
        ).firstOrNull()
    }
}