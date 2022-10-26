package cn.sunline.saas.workflow.defintion.modules.dto

import cn.sunline.saas.workflow.defintion.modules.DefinitionStatus
import java.util.*

data class DTOProcessDefinition(
    val name: String,
    val description: String? = null,
)

data class DTOProcessDefinitionView(
    val id: String,
    val name: String,
    val status: DefinitionStatus,
    val description:String?,
    var created: String,
)