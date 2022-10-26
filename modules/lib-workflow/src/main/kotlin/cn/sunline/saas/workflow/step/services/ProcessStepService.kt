package cn.sunline.saas.workflow.step.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.workflow.defintion.modules.DefinitionStatus
import cn.sunline.saas.workflow.defintion.modules.db.ProcessDefinition
import cn.sunline.saas.workflow.defintion.repositories.ProcessDefinitionRepository
import cn.sunline.saas.workflow.defintion.services.ProcessDefinitionService
import cn.sunline.saas.workflow.step.exception.ProcessStepNotFoundException
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.db.ProcessStep
import cn.sunline.saas.workflow.step.modules.dto.DTOProcessChange
import cn.sunline.saas.workflow.step.modules.dto.DTOProcessStepAdd
import cn.sunline.saas.workflow.step.repositories.ProcessStepRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Service
class ProcessStepService (
    private val processStepRepository: ProcessStepRepository,
    private val sequence: Sequence,
    private val processDefinitionService: ProcessDefinitionService,
    private val tenantDateTime: TenantDateTime
): BaseMultiTenantRepoService<ProcessStep, Long>(processStepRepository) {

    fun addOne(dtoProcessStepAdd: DTOProcessStepAdd):ProcessStep{
        return save(
            ProcessStep(
                id = sequence.nextId(),
                processDefinition = processDefinitionService.detail(dtoProcessStepAdd.processId),
                start = tenantDateTime.now().toDate()
            )
        )
    }

    fun updateOne(dtoProcessChange: DTOProcessChange):ProcessStep{
        val process = getOne(dtoProcessChange.id)?: throw ProcessStepNotFoundException("Invalid process!!")
        process.end = dtoProcessChange.end
        dtoProcessChange.startActivity?.run { process.startActivity = this }
        dtoProcessChange.status?.run { process.status = this }
        return save(process)
    }

    fun getPaged(name:String?,status: StepStatus?,pageable: Pageable):Page<ProcessStep>{
        return getPageWithTenant({ root, criteriaQuery, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            val processDefinition = root.join<ProcessStep,ProcessDefinition>("processDefinition",JoinType.INNER)
            name?.run { predicates.add(criteriaBuilder.like(processDefinition.get("name"),"$this%")) }
            status?.run { predicates.add(criteriaBuilder.equal(root.get<StepStatus>("status"),this)) }
            val orderBy = criteriaBuilder.desc(root.get<Long>("id"))
            criteriaQuery.orderBy(orderBy).where(*(predicates.toTypedArray())).restriction
        },pageable)
    }
}