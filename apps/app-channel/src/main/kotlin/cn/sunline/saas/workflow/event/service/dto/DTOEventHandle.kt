package cn.sunline.saas.workflow.event.service.dto

import cn.sunline.saas.workflow.defintion.modules.EventType
import cn.sunline.saas.workflow.step.modules.StepStatus
import java.util.*

data class DTOEventHandleView(
    val id:String,
    val processName:String,
    val activityName:String,
    val eventType: EventType,
    val position:String,
    val user:String? = null,
    val status:StepStatus,
    val start:Date? = null,
    val end:Date? = null,
)

data class DTOEventHandle(
    val applicationId:Long,
    val status: StepStatus,
    val user: String? = null,
    val data:Any?
)

data class DTOEventHandleDetail(
    val id:String,
    val applicationId: String,
    val nextPosition:String? = null,
    val data:Any? = null
)