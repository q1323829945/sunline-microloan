package cn.sunline.saas.workflow.definition.service

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.workflow.defintion.modules.DefinitionStatus
import cn.sunline.saas.workflow.defintion.modules.db.ProcessDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOProcessDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOProcessDefinitionView
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
class AppProcessDefinitionService(
    private val tenantDateTime: TenantDateTime
) {
    @Autowired
    private lateinit var processDefinitionService: ProcessDefinitionService

    private val objectMapper: ObjectMapper =
        jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun addOne(dtoProcessDefinition: DTOProcessDefinition): DTOProcessDefinitionView {
        val process = processDefinitionService.addOne(dtoProcessDefinition)
        return convert(process)
    }

    fun getPaged(status: DefinitionStatus?, pageable: Pageable): Page<DTOProcessDefinitionView> {
        return processDefinitionService.getProcessPaged(status, pageable).map {
            convert(it)
        }
    }

    fun updateStatus(id: Long, status: DefinitionStatus): DTOProcessDefinitionView {
        val process = processDefinitionService.updateStatus(id, status)
        return convert(process)
    }

    fun updateOne(id: Long, dtoProcessDefinition: DTOProcessDefinition): DTOProcessDefinitionView {
        val oldOne = processDefinitionService.preflightCheckProcessStatus(id)
        val process = processDefinitionService.updateOne(oldOne, dtoProcessDefinition)
        return convert(process)
    }

    private fun convert(processDefinition: ProcessDefinition):DTOProcessDefinitionView{
        val dtoProcessDefinition = objectMapper.convertValue<DTOProcessDefinitionView>(processDefinition)
        dtoProcessDefinition.created = tenantDateTime.toTenantDateTime(processDefinition.created).toString()
        return dtoProcessDefinition
    }
}
