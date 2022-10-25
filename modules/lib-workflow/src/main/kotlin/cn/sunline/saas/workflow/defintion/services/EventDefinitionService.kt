package cn.sunline.saas.workflow.defintion.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.seq.Sequence
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

@Service
class EventDefinitionService (
    private val eventDefinitionRepository: EventDefinitionRepository
): BaseMultiTenantRepoService<EventDefinition, String>(eventDefinitionRepository) {

    val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun addOne(dtoEventDefinition: DTOEventDefinition):EventDefinition{
        val eventDefinition = objectMapper.convertValue<EventDefinition>(dtoEventDefinition)
        return save(eventDefinition)
    }

    fun findById(id:String):EventDefinition?{
        return getOne(id)
    }

    fun findAll():Page<EventDefinition>{
        return getPageWithTenant(null, Pageable.unpaged())
    }

}