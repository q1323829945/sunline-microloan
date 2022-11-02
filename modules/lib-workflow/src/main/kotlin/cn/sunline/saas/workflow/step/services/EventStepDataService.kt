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

    fun addData(dtoEventStepData: DTOEventStepData):EventStepData{
        val eventStepData = getOne(dtoEventStepData.id)
        return if(eventStepData != null){
            updateOne(eventStepData,dtoEventStepData)
        } else {
            addOne(dtoEventStepData)
        }
    }

    fun addOne(dtoEventStepData: DTOEventStepData):EventStepData{
        return save(
            EventStepData(
                id = dtoEventStepData.id,
                applicationId = dtoEventStepData.applicationId,
                data = dtoEventStepData.data
            )
        )
    }


    fun updateOne(eventStepData: EventStepData,dtoEventStepData: DTOEventStepData):EventStepData{
        dtoEventStepData.data?.run { eventStepData.data = this }
        return save(eventStepData)
    }
}