package cn.sunline.saas.workflow.definition.service

import cn.sunline.saas.workflow.defintion.exception.ActivityDefinitionNotFoundException
import cn.sunline.saas.workflow.defintion.exception.EventTypeHasBeenUsedException
import cn.sunline.saas.workflow.defintion.modules.dto.DTOActivityDefinitionView
import cn.sunline.saas.workflow.defintion.modules.dto.DTOEventDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOEventDefinitionView
import cn.sunline.saas.workflow.defintion.services.ActivityDefinitionService
import cn.sunline.saas.workflow.defintion.services.EventDefinitionService
import cn.sunline.saas.workflow.defintion.services.ProcessDefinitionService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AppEventDefinitionService {
    @Autowired
    private lateinit var eventDefinitionService: EventDefinitionService

    @Autowired
    private lateinit var activityDefinitionService: ActivityDefinitionService

    @Autowired
    private lateinit var processDefinitionService: ProcessDefinitionService

    val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun addOne(dtoEventDefinition: DTOEventDefinition):DTOEventDefinitionView{
        preflightEventCheckType(dtoEventDefinition)
        val event = eventDefinitionService.addOne(dtoEventDefinition)
        return objectMapper.convertValue(event)
    }

    fun updateOne(id:Long,dtoEventDefinition: DTOEventDefinition):DTOEventDefinitionView{
        preflightEventCheck(dtoEventDefinition)
        val event = eventDefinitionService.updateOne(id, dtoEventDefinition)
        return objectMapper.convertValue(event)
    }

    fun getPaged(activityId:Long,pageable: Pageable): Page<DTOEventDefinitionView> {
        val paged = eventDefinitionService.findPagedByActivity(activityId, pageable)
        return paged.map { objectMapper.convertValue<DTOEventDefinitionView>(it) }
    }

    private fun preflightEventCheckType(dtoEventDefinition: DTOEventDefinition){
        val activities = activityDefinitionService.findPagedByProcess(dtoEventDefinition.processId, Pageable.unpaged())
        activities.forEach { activity ->
            activity.events.forEach {
                if(it.type == dtoEventDefinition.type){
                    throw EventTypeHasBeenUsedException("The type has been used")
                }
            }
        }

        preflightEventCheck(dtoEventDefinition)
    }

    private fun preflightEventCheck(dtoEventDefinition: DTOEventDefinition){
        processDefinitionService.preflightCheckProcessStatus(dtoEventDefinition.processId)
        activityDefinitionService.getOne(dtoEventDefinition.activityId)?:throw ActivityDefinitionNotFoundException("Invalid activity")
    }
}