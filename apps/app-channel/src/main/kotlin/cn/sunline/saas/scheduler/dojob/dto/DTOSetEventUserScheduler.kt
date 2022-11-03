package cn.sunline.saas.scheduler.dojob.dto


data class DTOSetEventUserScheduler (
    val applicationId:String,
    val eventStepId: String,
    val position:String,
    val isCurrentEventStep:Boolean = false
)

