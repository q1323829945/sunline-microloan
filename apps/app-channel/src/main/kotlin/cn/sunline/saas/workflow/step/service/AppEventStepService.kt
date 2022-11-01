package cn.sunline.saas.workflow.step.service

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.workflow.step.modules.db.EventStep
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepView
import cn.sunline.saas.workflow.step.services.EventStepService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AppEventStepService(private val tenantDateTime: TenantDateTime) {
    @Autowired
    private lateinit var eventStepService: EventStepService

    fun paged(activityStepId:Long, pageable: Pageable): Page<DTOEventStepView> {
        return eventStepService.getPaged(activityStepId, pageable).map {
            convert(it)
        }
    }

    private fun convert(eventStep: EventStep): DTOEventStepView {
        return DTOEventStepView(
            id = eventStep.id.toString(),
            name = eventStep.eventDefinition.name,
            type = eventStep.eventDefinition.type,
            description = eventStep.eventDefinition.description,
            user = eventStep.user,
            status = eventStep.status,
            next = eventStep.next?.toString(),
            start = eventStep.start?.run { tenantDateTime.toTenantDateTime(this).toString() },
            end = eventStep.end?.run { tenantDateTime.toTenantDateTime(this).toString() }
        )
    }
}