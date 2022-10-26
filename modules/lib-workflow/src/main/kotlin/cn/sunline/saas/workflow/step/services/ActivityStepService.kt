package cn.sunline.saas.workflow.step.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.workflow.defintion.modules.db.ProcessDefinition
import cn.sunline.saas.workflow.defintion.repositories.ProcessDefinitionRepository
import cn.sunline.saas.workflow.defintion.services.ActivityDefinitionService
import cn.sunline.saas.workflow.step.exception.ActivityStepNotFoundException
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.db.ActivityStep
import cn.sunline.saas.workflow.step.modules.db.ProcessStep
import cn.sunline.saas.workflow.step.modules.dto.DTOActivityStepAdd
import cn.sunline.saas.workflow.step.modules.dto.DTOActivityStepChange
import cn.sunline.saas.workflow.step.repositories.ActivityStepRepository
import cn.sunline.saas.workflow.step.repositories.ProcessStepRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Service
class ActivityStepService (
    private val activityStepRepository: ActivityStepRepository,
    private val activityDefinitionService: ActivityDefinitionService,
    private val sequence: Sequence
): BaseMultiTenantRepoService<ActivityStep, Long>(activityStepRepository) {

    fun addOne(dtoActivityStepAdd: DTOActivityStepAdd):ActivityStep{
        val activityDefinition = activityDefinitionService.detail(dtoActivityStepAdd.activityId)
        return save(
            ActivityStep(
                id = sequence.nextId(),
                processStepId = dtoActivityStepAdd.processStepId,
                activityDefinition = activityDefinition,
                user = dtoActivityStepAdd.user,
                sort = activityDefinition.sort
            )
        )
    }

    fun updateOne(dtoActivityStepChange: DTOActivityStepChange):ActivityStep{
        val activity = getOne(dtoActivityStepChange.id)?: throw ActivityStepNotFoundException("Invalid activity !!")
        dtoActivityStepChange.user?.run { activity.user = this }
        dtoActivityStepChange.start?.run { activity.start = this }
        dtoActivityStepChange.end?.run { activity.end = this }
        dtoActivityStepChange.status?.run { activity.status = this }
        dtoActivityStepChange.next?.run { activity.next = this }
        return save(activity)
    }

    fun getPaged(processStepId:Long,pageable: Pageable):Page<ActivityStep>{
        return getPageWithTenant({ root, criteriaQuery, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("processStepId"),processStepId))
            val orderBySort = criteriaBuilder.asc(root.get<Long>("sort"))
            val orderById = criteriaBuilder.desc(root.get<Long>("id"))
            criteriaQuery.orderBy(orderBySort,orderById).where(*(predicates.toTypedArray())).restriction
        },pageable)
    }
}