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

    fun setNext(user:String,eventStep: EventStep,applicationId:Long,data:Any? = null){
        setNextEventStepStart(user, eventStep,applicationId, data)
    }

    fun setCurrent(user:String,eventStep: EventStep,applicationId:Long,data:Any? = null){
        setCurrentEventStepStart(user, eventStep, applicationId, data)
    }

    fun handleNext(user: String?,eventStep: EventStep,applicationId:Long,data:Any? = null){
        if(user!= null){
            setNext(user,eventStep,applicationId,data)
        } else {
            toActorEventHandle(eventStep,applicationId,data)
        }
    }

    private fun toActorEventHandle(eventStep: EventStep,applicationId:Long,data:Any? = null){
        val activity = getActivity(eventStep.activityStepId)!!

        val dtoSetEventUserScheduler = DTOSetEventUserScheduler(
            applicationId = applicationId.toString(),
            eventStepId = eventStep.id.toString(),
            position = activity.activityDefinition.position,
            body = data,
            isCurrentEventStep = false
        )

        createScheduler.create(ActorType.SET_EVENT_USER,eventStep.id.toString(),dtoSetEventUserScheduler)
    }

    protected fun rejected(eventStep: EventStep){
        eventStepService.updateOne(eventStep.id,
            DTOEventStepChange(
                status = StepStatus.REJECTED,
                end = tenantDateTime.now().toDate()
            )
        )
        getProcess(eventStep)?.run {
            createScheduler.create(ActorType.FINISH_EVENT_HANDLE,this.id.toString())
        }
    }

}