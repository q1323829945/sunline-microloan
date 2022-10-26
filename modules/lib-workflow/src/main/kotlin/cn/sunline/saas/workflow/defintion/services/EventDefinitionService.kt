package cn.sunline.saas.workflow.defintion.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.workflow.defintion.exception.EventDefinitionNotFoundException
import cn.sunline.saas.workflow.defintion.modules.db.EventDefinition
import cn.sunline.saas.workflow.defintion.modules.db.ProcessDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOEventDefinition
import cn.sunline.saas.workflow.defintion.repositories.EventDefinitionRepository
import cn.sunline.saas.workflow.defintion.repositories.ProcessDefinitionRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class EventDefinitionService (
    private val eventDefinitionRepository: EventDefinitionRepository,
    private val sequence: Sequence,
): BaseMultiTenantRepoService<EventDefinition, Long>(eventDefinitionRepository) {


    fun addOne(dtoEventDefinition: DTOEventDefinition):EventDefinition{
        return save(EventDefinition(
            id = sequence.nextId(),
            activityId = dtoEventDefinition.activityId,
            name = dtoEventDefinition.type.id,
            type = dtoEventDefinition.type,
            description = dtoEventDefinition.type.description,
            sort = dtoEventDefinition.sort
        ))
    }

    fun updateOne(id: Long,dtoEventDefinition: DTOEventDefinition):EventDefinition{
        val event = getOne(id)?: throw EventDefinitionNotFoundException("Invalid event definition !!")
        event.sort = dtoEventDefinition.sort
        return save(event)
    }

    fun findPagedByActivity(activityId:Long,pageable:Pageable):Page<EventDefinition>{
        return getPageWithTenant({ root, criteriaQuery, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("activityId"),activityId))
            val orderBySort = criteriaBuilder.asc(root.get<Long>("sort"))
            val orderById = criteriaBuilder.desc(root.get<Long>("id"))
            criteriaQuery.orderBy(orderBySort,orderById).where(*(predicates.toTypedArray())).restriction
        },pageable)
    }

}