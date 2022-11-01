package cn.sunline.saas.workflow.step.modules.dto

data class DTOEventStepData(
    val id:Long,
    val applicationId:Long,
    val data:String? = null,
)