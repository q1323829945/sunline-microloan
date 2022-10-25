package cn.sunline.saas.workflow.defintion.modules.dto

import cn.sunline.saas.workflow.defintion.modules.EventType

data class DTOEventDefinition(
    val id:String,
    val name: String,
    val type: EventType,
    val description: String? = null,
)

data class DTOEventDefinitionView(
    val id: String,
    val name: String,
    val type: EventType,
    val description: String? = null,
)