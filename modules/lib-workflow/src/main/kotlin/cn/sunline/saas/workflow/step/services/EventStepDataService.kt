package cn.sunline.saas.workflow.step.services

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.workflow.step.modules.db.EventStepData
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepData
import cn.sunline.saas.workflow.step.repositories.EventStepDataRepository
import org.springframework.stereotype.Service

@Service
class EventStepDataService (
    private val eventStepDataRepository: EventStepDataRepository,
): BaseMultiTenantRepoService<EventStepData, Long>(eventStepDataRepository) {

    fun addOne(dtoEventStepData: DTOEventStepData):EventStepData{
        return save(
            EventStepData(
                id = dtoEventStepData.id,
                applicationId = dtoEventStepData.applicationId,
                data = dtoEventStepData.data
            )
        )
    }
}