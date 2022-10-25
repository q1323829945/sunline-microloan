package cn.sunline.saas.workflow.defintion.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.workflow.defintion.exception.ActivityDefinitionNotFoundException
import cn.sunline.saas.workflow.defintion.modules.DefinitionStatus
import cn.sunline.saas.workflow.defintion.modules.db.ActivityDefinition
import cn.sunline.saas.workflow.defintion.modules.db.EventDefinition
import cn.sunline.saas.workflow.defintion.modules.db.ProcessDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOActivityDefinition
import cn.sunline.saas.workflow.defintion.repositories.ActivityDefinitionRepository
import cn.sunline.saas.workflow.defintion.repositories.ProcessDefinitionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class ActivityDefinitionService (
    private val activityDefinitionRepository: ActivityDefinitionRepository,
    private val sequence: Sequence,
    private val eventDefinitionService: EventDefinitionService,
): BaseMultiTenantRepoService<ActivityDefinition, Long>(activityDefinitionRepository) {

    fun addOne(dtoActivityDefinition: DTOActivityDefinition):ActivityDefinition{
        val events = getEvents(dtoActivityDefinition.eventIds)
        return save(
            ActivityDefinition(
                id = sequence.nextId(),
                processId = dtoActivityDefinition.processId,
                name = dtoActivityDefinition.name,
                position = dtoActivityDefinition.position,
                description = dtoActivityDefinition.description,
                events = events
            )
        )
    }


    fun getPagedByProcess(processId:Long,pageable: Pageable):Page<ActivityDefinition>{
        return getPageWithTenant({ root, criteriaQuery, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("processId"),processId))
            val orderBy = criteriaBuilder.desc(root.get<Long>("id"))
            criteriaQuery.orderBy(orderBy).where(*(predicates.toTypedArray())).restriction
        },pageable)
    }

    fun updateOne(id: Long,dtoActivityDefinition: DTOActivityDefinition):ActivityDefinition{
        val oldOne = getOne(id)?: throw ActivityDefinitionNotFoundException("Invalid activity!!")
        oldOne.name = dtoActivityDefinition.name
        oldOne.description = dtoActivityDefinition.description
        oldOne.position = dtoActivityDefinition.position

        val events = getEvents(dtoActivityDefinition.eventIds)
        oldOne.events = events
        return save(oldOne)
    }


    private fun getEvents(ids:List<String>):MutableList<EventDefinition>{
        val events = mutableListOf<EventDefinition>()
        ids.forEach { id ->
            eventDefinitionService.getOne(id)?.run {
                events.add(this)
            }
        }
        return events
    }
}