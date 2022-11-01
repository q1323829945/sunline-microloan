package cn.sunline.saas.workflow.step.modules.dto

import cn.sunline.saas.workflow.step.modules.StepStatus
import java.util.*

data class DTOActivityStepAdd(
    val processStepId:Long,
    val activityId:Long,
    val next: Long? = null
)

data class DTOActivityStepChange(
    val status: StepStatus? = null,
    val start:Date? = null,
    val end:Date? = null,
)

data class DTOActivityStepView(
    val id:String,
    val name:String,
    val position:String? = null,
    val description:String? = null,
    val processStepId:String,
    val activityId:String,
    val status: StepStatus,
    val next: String? = null,
    val start:String? = null,
    val end:String? = null,
)
