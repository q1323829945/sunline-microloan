package cn.sunline.saas.workflow.step.modules.dto

import cn.sunline.saas.workflow.step.modules.StepStatus
import java.util.Date

data class DTOProcessStepAdd(
    val processId:Long,
)

data class DTOProcessChange(
    val id:Long,
    val startActivity:Long? = null,
    val status: StepStatus? = null,
    val end:Date? = null,
)

data class DTOProcessStepView(
    val id:String,
    val name:String,
    val description:String? = null,
    val processId:String,
    val status:StepStatus,
    val startActivity:String? = null,
    val start:String,
    val end:String? = null,
)

