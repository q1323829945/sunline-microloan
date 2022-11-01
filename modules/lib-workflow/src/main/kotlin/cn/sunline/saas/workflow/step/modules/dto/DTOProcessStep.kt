package cn.sunline.saas.workflow.step.modules.dto

import cn.sunline.saas.workflow.step.modules.StepStatus
import java.util.*

data class DTOProcessStepAdd(
    val processId:Long,
)

data class DTOProcessStepChange(
    val status: StepStatus? = null,
    val end:Date? = null,
)

data class DTOProcessStepView(
    val id:String,
    val name:String,
    val description:String? = null,
    val processId:String,
    val status:StepStatus,
    val start:String,
    val end:String? = null,
)

