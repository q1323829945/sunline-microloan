package cn.sunline.saas.workflow.step.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.workflow.defintion.modules.db.ProcessDefinition
import cn.sunline.saas.workflow.defintion.repositories.ProcessDefinitionRepository
import cn.sunline.saas.workflow.defintion.services.EventDefinitionService
import cn.sunline.saas.workflow.step.exception.EventStepNotFoundException
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.db.ActivityStep
import cn.sunline.saas.workflow.step.modules.db.EventStep
import cn.sunline.saas.workflow.step.modules.db.ProcessStep
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepAdd
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepChange
import cn.sunline.saas.workflow.step.repositories.EventStepRepository
import cn.sunline.saas.workflow.step.repositories.ProcessStepRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class EventStepService (
    private val eventStepRepository: EventStepRepository,
    private val eventDefinitionService: EventDefinitionService,
    private val sequence: Sequence,
): BaseMultiTenantRepoService<EventStep, Long>(eventStepRepository) {

    fun addOne(dtoEventStepAdd: DTOEventStepAdd):EventStep{
        val eventDefinition = eventDefinitionService.detail(dtoEventStepAdd.eventId)
        return save(
            EventStep(
                id = sequence.nextId(),
                activityStepId = dtoEventStepAdd.activityStepId,
                eventDefinition = eventDefinition,
                sort = eventDefinition.sort,
            )
        )
    }

    fun updateOne(dtoEventStepChange: DTOEventStepChange):EventStep{
        val event = getOne(dtoEventStepChange.id)?: throw EventStepNotFoundException("Invalid event !!")
        dtoEventStepChange.status?.run { event.status = this }
        dtoEventStepChange.next?.run { event.next = this }
        dtoEventStepChange.start?.run { event.start = this }
        dtoEventStepChange.end?.run { event.end = this }
        return save(event)
    }

    fun getPaged(activityStepId:Long,pageable: Pageable): Page<EventStep> {
        return getPageWithTenant({ root, criteriaQuery, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("activityStepId"),activityStepId))
            val orderBySort = criteriaBuilder.asc(root.get<Long>("sort"))
            val orderById = criteriaBuilder.desc(root.get<Long>("id"))
            criteriaQuery.orderBy(orderBySort,orderById).where(*(predicates.toTypedArray())).restriction
        },pageable)
    }
}