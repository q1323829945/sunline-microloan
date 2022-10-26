package cn.sunline.saas.workflow.step.service

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.db.ProcessStep
import cn.sunline.saas.workflow.step.modules.dto.DTOProcessStepView
import cn.sunline.saas.workflow.step.services.ProcessStepService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AppProcessStepService(private val tenantDateTime: TenantDateTime) {
    @Autowired
    private lateinit var processStepService: ProcessStepService

    fun paged(name:String?,status:StepStatus?,pageable: Pageable):Page<DTOProcessStepView>{
        return processStepService.getPaged(name, status, pageable).map {
            convert(it)
        }
    }

    private fun convert(processStep: ProcessStep):DTOProcessStepView{
        return DTOProcessStepView(
            id = processStep.id.toString(),
            name = processStep.processDefinition.name,
            description = processStep.processDefinition.description,
            processId = processStep.processDefinition.id.toString(),
            status = processStep.status,
            startActivity = processStep.startActivity?.toString(),
            start = tenantDateTime.toTenantDateTime(processStep.start).toString(),
            end = processStep.end?.run { tenantDateTime.toTenantDateTime(this).toString() }
        )
    }
}