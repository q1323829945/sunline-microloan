package cn.sunline.saas.workflow.defintion.modules.dto

import cn.sunline.saas.workflow.defintion.modules.EventType

data class DTOEventDefinition(
    val processId:Long,
    val activityId:Long,
    val type: EventType,
    val sort:Long,
)

data class DTOEventDefinitionView(
    val id: Long,
    val activityId:Long,
    val name: String,
    val type: EventType,
    val sort:Long,
    val description: String? = null,
)