package cn.sunline.saas.workflow.definition.service

import cn.sunline.saas.workflow.defintion.modules.dto.DTOActivityDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOActivityDefinitionView
import cn.sunline.saas.workflow.defintion.services.ActivityDefinitionService
import cn.sunline.saas.workflow.defintion.services.ProcessDefinitionService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AppActivityDefinitionService{
    @Autowired
    private lateinit var activityDefinitionService: ActivityDefinitionService

    @Autowired
    private lateinit var processDefinitionService: ProcessDefinitionService

    private val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun addOne(dtoActivityDefinition: DTOActivityDefinition):DTOActivityDefinitionView{
        processDefinitionService.preflightCheckProcessStatus(dtoActivityDefinition.processId)
        val activity = activityDefinitionService.addOne(dtoActivityDefinition)
        return objectMapper.convertValue(activity)
    }

    fun updateOne(id:Long,dtoActivityDefinition: DTOActivityDefinition):DTOActivityDefinitionView{
        processDefinitionService.preflightCheckProcessStatus(dtoActivityDefinition.processId)
        val activity = activityDefinitionService.updateOne(id, dtoActivityDefinition)
        return objectMapper.convertValue(activity)
    }

    fun getPaged(processId:Long,pageable: Pageable):Page<DTOActivityDefinitionView>{
        val paged = activityDefinitionService.findPagedByProcess(processId, pageable)
        return paged.map { objectMapper.convertValue<DTOActivityDefinitionView>(it) }
    }

    @Transactional
    fun delete(id:Long):DTOActivityDefinitionView{
        val activity = activityDefinitionService.detail(id)
        processDefinitionService.preflightCheckProcessStatus(activity.processId)
        activityDefinitionService.remove(activity)
        return objectMapper.convertValue(activity)
    }
}