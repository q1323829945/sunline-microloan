package cn.sunline.saas.workflow.definition.service

import cn.sunline.saas.workflow.defintion.exception.ProcessDefinitionUpdateException
import cn.sunline.saas.workflow.defintion.modules.DefinitionStatus
import cn.sunline.saas.workflow.defintion.modules.dto.DTOProcessDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOProcessDefinitionView
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

@Service
class AppProcessDefinitionService {
    @Autowired
    private lateinit var processDefinitionService: ProcessDefinitionService

    private val objectMapper: ObjectMapper =
        jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun addOne(dtoProcessDefinition: DTOProcessDefinition): DTOProcessDefinitionView {
        val process = processDefinitionService.addOne(dtoProcessDefinition)
        return objectMapper.convertValue(process)
    }

    fun getPaged(status: DefinitionStatus?, pageable: Pageable): Page<DTOProcessDefinitionView> {
        return processDefinitionService.getProcessPaged(status, pageable).map {
            objectMapper.convertValue(it)
        }
    }

    fun updateStatus(id: Long, status: DefinitionStatus): DTOProcessDefinitionView {
        val process = processDefinitionService.updateStatus(id, status)
        return objectMapper.convertValue(process)
    }

    fun updateOne(id: Long, dtoProcessDefinition: DTOProcessDefinition): DTOProcessDefinitionView {
        val oldOne = processDefinitionService.preflightCheckProcessStatus(id)
        val process = processDefinitionService.updateOne(oldOne, dtoProcessDefinition)
        return objectMapper.convertValue(process)
    }
}
