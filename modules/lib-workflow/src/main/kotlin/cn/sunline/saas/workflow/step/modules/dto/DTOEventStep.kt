package cn.sunline.saas.workflow.step.modules.dto

import cn.sunline.saas.workflow.defintion.modules.EventType
import cn.sunline.saas.workflow.step.modules.StepStatus
import java.util.*

data class DTOEventStepAdd(
    val activityStepId:Long,
    val eventId:Long,
    val next: Long? = null,
    val user:String? = null,
)

data class DTOEventStepChange(
    val user:String? = null,
    val status: StepStatus? = null,
    val start: Date? = null,
    val end: Date? = null,
)

data class DTOEventStepView(
    val id:String,
    val name:String,
    val type: EventType,
    val description:String? = null,
    val user:String? = null,
    val status: StepStatus,
    val next:String? = null,
    val start:String? = null,
    val end:String? = null,
)


