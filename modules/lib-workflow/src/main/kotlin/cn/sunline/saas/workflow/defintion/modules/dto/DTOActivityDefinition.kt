package cn.sunline.saas.workflow.defintion.modules.dto

import javax.persistence.Column
import javax.persistence.Id
import javax.validation.constraints.NotNull

data class DTOActivityDefinition(
    val processId: Long,
    val name: String,
    val position: String? = null,
    val description: String? = null,
    val sort:Long,
)

data class DTOActivityDefinitionView(
    val id:String,
    val processId: String,
    val name: String,
    val position: String? = null,
    val description: String? = null,
    val sort:Long
)