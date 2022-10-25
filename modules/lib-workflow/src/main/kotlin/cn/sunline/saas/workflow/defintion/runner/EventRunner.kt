package cn.sunline.saas.workflow.defintion.runner

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.workflow.defintion.modules.EventType
import cn.sunline.saas.workflow.defintion.modules.dto.DTOEventDefinition
import cn.sunline.saas.workflow.defintion.services.EventDefinitionService
import org.springframework.stereotype.Component

@Component
class EventRunner(private val eventDefinitionService: EventDefinitionService) {

    fun run() {
        EventType.values().forEach {
            eventDefinitionService.findById("${it.id}_${ContextUtil.getTenant()}")?:run {
                eventDefinitionService.addOne(
                    DTOEventDefinition(
                        id = "${it.id}_${ContextUtil.getTenant()}",
                        name = it.id,
                        type = it,
                        description = it.description
                    )
                )
            }
        }
    }
}