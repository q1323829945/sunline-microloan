package cn.sunline.saas.workflow.defintion.modules.dto

data class DTOActivityDefinition(
    val processId: Long,
    val name: String,
    val position: String,
    val description: String? = null,
    val sort:Long = 1,
)

data class DTOActivityDefinitionView(
    val id:String,
    val processId: String,
    val name: String,
    val position: String,
    val description: String? = null,
    val sort:Long
)