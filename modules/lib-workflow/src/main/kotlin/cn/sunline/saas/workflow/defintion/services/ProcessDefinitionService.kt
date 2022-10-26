package cn.sunline.saas.workflow.defintion.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.workflow.defintion.exception.ProcessDefinitionAlreadyExistException
import cn.sunline.saas.workflow.defintion.exception.ProcessDefinitionCodeException
import cn.sunline.saas.workflow.defintion.exception.ProcessDefinitionNotFoundException
import cn.sunline.saas.workflow.defintion.exception.ProcessDefinitionUpdateException
import cn.sunline.saas.workflow.defintion.modules.DefinitionStatus
import cn.sunline.saas.workflow.defintion.modules.db.ProcessDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOProcessDefinition
import cn.sunline.saas.workflow.defintion.repositories.ProcessDefinitionRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class ProcessDefinitionService (
    private val processDefinitionRepository: ProcessDefinitionRepository,
    private val sequence: Sequence,
    private val tenantDateTime: TenantDateTime
): BaseMultiTenantRepoService<ProcessDefinition, Long>(processDefinitionRepository) {

    fun addOne(dtoProcessDefinition: DTOProcessDefinition):ProcessDefinition{
        return save(
            ProcessDefinition(
                id = sequence.nextId(),
                name = dtoProcessDefinition.name,
                description = dtoProcessDefinition.description,
                created = tenantDateTime.now().toDate()
            )
        )
    }

    fun updateOne(oldOne:ProcessDefinition,dtoProcessDefinition: DTOProcessDefinition):ProcessDefinition{
        oldOne.name = dtoProcessDefinition.name
        oldOne.description = dtoProcessDefinition.description
        return save(oldOne)
    }

    fun getProcessPaged(status: DefinitionStatus?,pageable: Pageable):Page<ProcessDefinition>{
        return getPageWithTenant({ root, criteriaQuery, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            status?.run { predicates.add(criteriaBuilder.equal(root.get<DefinitionStatus>("status"),status)) }
            val orderBy = criteriaBuilder.desc(root.get<Long>("id"))
            criteriaQuery.orderBy(orderBy).where(*(predicates.toTypedArray())).restriction
        }, Pageable.unpaged())
    }

    fun updateStatus(id: Long,status: DefinitionStatus):ProcessDefinition{
        val process = preflightCheckProcess(id)

        if(status == DefinitionStatus.NOT_START){
            throw ProcessDefinitionUpdateException("This process can't update!")
        }

        process.status = status

        if(status == DefinitionStatus.ACTIVE){
            getProcessPaged(DefinitionStatus.ACTIVE, Pageable.unpaged()).firstOrNull()?.run {
                this.status = DefinitionStatus.INACTIVE
                save(this)
            }
        }

        return save(process)
    }

    fun preflightCheckProcess(id: Long): ProcessDefinition {
        return getOne(id) ?: throw ProcessDefinitionNotFoundException("Invalid process!!")
    }

    fun preflightCheckProcessStatus(id: Long):ProcessDefinition{
        val process = preflightCheckProcess(id)
        if (process.status != DefinitionStatus.NOT_START) {
            throw ProcessDefinitionUpdateException("Status is not NOT_START!!")
        }
        return process
    }
}