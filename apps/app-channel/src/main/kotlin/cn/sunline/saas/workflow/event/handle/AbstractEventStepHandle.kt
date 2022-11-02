package cn.sunline.saas.workflow.event.handle

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.db.EventStep
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepChange
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepData
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepDataService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Pageable
import javax.persistence.criteria.Predicate

abstract class AbstractEventStepHandle(
    private val eventStepService: EventStepService,
    private val eventStepDataService: EventStepDataService,
    activityStepService: ActivityStepService,
    processStepService: ProcessStepService,
    private val tenantDateTime: TenantDateTime,
):AbstractActivityStepHandle(activityStepService, processStepService, tenantDateTime) {

    private val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    protected fun setNextEventStepStart(user:String,eventStep: EventStep,applicationId:Long,data:Any? = null){
        eventStep.next?.let {
            val nextEventStep = getNextStep(it)
            nextEventStep?.run { setEventStepStart(user, this,applicationId,data) }
        }?: run {
            setNextActivityFirstEventStart(user, eventStep.activityStepId,applicationId,data)
        }
    }

    protected fun setCurrentEventStepStart(user:String, eventStep: EventStep,applicationId:Long,data:Any? = null){
        setEventStepStart(user, eventStep, applicationId, data)
        setCurrentActivityStart(eventStep.activityStepId)
    }

    private fun setNextActivityFirstEventStart(user: String,activityStepId:Long,applicationId:Long,data:Any? = null){
        val nextActivity = setActivityFinishAndGetNextActivity(activityStepId)
        nextActivity?.run {
            val event = getFirstEvent(this.id)
            event?.run {
                setEventStepStart(user,event,applicationId,data)
            }?: run {
                setActivityFinish(this.id)
            }
        }
    }

    private fun getNextStep(nextEventStepId:Long):EventStep?{
        return eventStepService.getOne(nextEventStepId)
    }
    private fun setEventStepStart(user:String, eventStep: EventStep,applicationId:Long,data:Any? = null){
        eventStepService.updateOne(eventStep.id,
            DTOEventStepChange(
                user = user,
                status = StepStatus.PROCESSING,
                start = tenantDateTime.now().toDate(),
            )
        )

        setEventStepData(eventStep.id,applicationId,data)
    }

    protected fun setEventStepData(eventStepId:Long,applicationId: Long,data:Any? = null){
        eventStepDataService.addData(
            DTOEventStepData(
                eventStepId,
                applicationId,
                data?.run {
                    if(this is String){
                        this
                    } else {
                        objectMapper.writeValueAsString(this)
                    }
                }
            )
        )
    }

    private fun getFirstEvent(activityStepId: Long):EventStep?{
        return eventStepService.getPageWithTenant({ root,query,builder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(builder.equal(root.get<Long>("activityStepId"),activityStepId))
            val orderBySort = builder.asc(root.get<Long>("sort"))
            val orderById = builder.desc(root.get<Long>("id"))
            query.orderBy(orderBySort,orderById).where(*(predicates.toTypedArray())).restriction
        }, Pageable.unpaged()).firstOrNull()
    }
}