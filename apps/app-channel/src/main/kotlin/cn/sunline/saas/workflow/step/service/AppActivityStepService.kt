package cn.sunline.saas.workflow.step.service

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.workflow.step.modules.db.ActivityStep
import cn.sunline.saas.workflow.step.modules.dto.DTOActivityStepView
import cn.sunline.saas.workflow.step.services.ActivityStepService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AppActivityStepService(private val tenantDateTime: TenantDateTime) {
    @Autowired
    private lateinit var activityStepService: ActivityStepService

    fun paged(processStepId:Long, pageable: Pageable): Page<DTOActivityStepView> {
        return activityStepService.getPaged(processStepId, pageable).map {
            convert(it)
        }
    }

    private fun convert(activityStep: ActivityStep): DTOActivityStepView {
        return DTOActivityStepView(
            id = activityStep.id.toString(),
            name = activityStep.activityDefinition.name,
            position = activityStep.activityDefinition.position,
            description = activityStep.activityDefinition.description,
            processStepId = activityStep.processStepId.toString(),
            activityId = activityStep.activityDefinition.id.toString(),
            status = activityStep.status,
            next = activityStep.next?.toString(),
            start = activityStep.start?.run { tenantDateTime.toTenantDateTime(this).toString() },
            end = activityStep.end?.run { tenantDateTime.toTenantDateTime(this).toString() }
        )
    }
}