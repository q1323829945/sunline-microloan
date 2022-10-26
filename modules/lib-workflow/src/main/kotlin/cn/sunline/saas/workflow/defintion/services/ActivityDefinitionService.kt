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
    private val sequence: Sequence
): BaseMultiTenantRepoService<ActivityDefinition, Long>(activityDefinitionRepository) {

    fun addOne(dtoActivityDefinition: DTOActivityDefinition):ActivityDefinition{
        return save(
            ActivityDefinition(
                id = sequence.nextId(),
                processId = dtoActivityDefinition.processId,
                name = dtoActivityDefinition.name,
                position = dtoActivityDefinition.position,
                description = dtoActivityDefinition.description,
                sort = dtoActivityDefinition.sort
            )
        )
    }


    fun findPagedByProcess(processId:Long,pageable: Pageable):Page<ActivityDefinition>{
        return getPageWithTenant({ root, criteriaQuery, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("processId"),processId))
            val orderBySort = criteriaBuilder.asc(root.get<Long>("sort"))
            val orderById = criteriaBuilder.desc(root.get<Long>("id"))
            criteriaQuery.orderBy(orderBySort,orderById).where(*(predicates.toTypedArray())).restriction
        },pageable)
    }

    fun updateOne(id: Long,dtoActivityDefinition: DTOActivityDefinition):ActivityDefinition{
        val oldOne = detail(id)
        oldOne.name = dtoActivityDefinition.name
        oldOne.description = dtoActivityDefinition.description
        oldOne.position = dtoActivityDefinition.position
        oldOne.sort = dtoActivityDefinition.sort
        return save(oldOne)
    }

    fun remove(activityDefinition: ActivityDefinition){
        activityDefinitionRepository.delete(activityDefinition)
    }

    fun detail(id:Long):ActivityDefinition{
        return getOne(id)?: throw ActivityDefinitionNotFoundException("Invalid activity definition !!")
    }

}