package cn.sunline.saas.workflow.event.handle

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.create.CreateScheduler
import cn.sunline.saas.scheduler.dojob.dto.DTOSetEventUserScheduler
import cn.sunline.saas.workflow.event.handle.helper.EventHandleCommand
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.db.EventStep
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepChange
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepDataService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

abstract class AbstractEventHandle(
    private val eventStepService: EventStepService,
    eventStepDataService: EventStepDataService,
    activityStepService: ActivityStepService,
    processStepService: ProcessStepService,
    private val tenantDateTime: TenantDateTime,
    private val createScheduler: CreateScheduler
):AbstractEventStepHandle(eventStepService,eventStepDataService, activityStepService, processStepService, tenantDateTime) {

    abstract fun doHandle(eventHandleCommand: EventHandleCommand)

    val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun setNext(user:String,eventStep: EventStep,applicationId:Long){
        setNextEventStepProcessing(user, eventStep,applicationId)
    }

    fun setCurrent(user:String,eventStep: EventStep,applicationId:Long){
        setCurrentEventStepProcessing(user, eventStep, applicationId)
    }

    protected fun handleNext(user: String?,eventStep: EventStep,applicationId:Long){
        if(user!= null){
            setNext(user,eventStep,applicationId)
        } else {
            toActorEventHandle(eventStep,applicationId)
        }
    }

    private fun toActorEventHandle(eventStep: EventStep,applicationId:Long){
        val activity = getActivity(eventStep.activityStepId)!!

        val dtoSetEventUserScheduler = DTOSetEventUserScheduler(
            applicationId = applicationId.toString(),
            eventStepId = eventStep.id.toString(),
            position = activity.activityDefinition.position,
            isCurrentEventStep = false
        )

        createScheduler.create(ActorType.SET_EVENT_USER,eventStep.id.toString(),dtoSetEventUserScheduler)
    }

    protected fun passed(eventStep: EventStep,applicationId: Long,data:Any? = null){
        eventStepService.updateOne(eventStep.id,
            DTOEventStepChange(
                status = StepStatus.PASSED,
                end = tenantDateTime.now().toDate()
            )
        )
        setEventStepData(eventStep.id,applicationId)
        setNextEventStepStart(eventStep,applicationId, data)
    }

    protected fun rejected(eventStep: EventStep,applicationId: Long,data:Any? = null){
        eventStepService.updateOne(eventStep.id,
            DTOEventStepChange(
                status = StepStatus.REJECTED,
                end = tenantDateTime.now().toDate()
            )
        )
        setEventStepData(eventStep.id,applicationId,data)

        getProcess(eventStep)?.run {
            createScheduler.create(ActorType.FINISH_EVENT_HANDLE,this.id.toString())
        }
    }

}