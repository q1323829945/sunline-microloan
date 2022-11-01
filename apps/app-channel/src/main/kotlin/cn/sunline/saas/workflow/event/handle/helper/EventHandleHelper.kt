package cn.sunline.saas.workflow.event.handle.helper

import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.db.EventStep
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging

data class EventHandleCommand(
    val applicationId:Long,
    val eventStep: EventStep,
    val status: StepStatus,
    val user:String? = null,
    val data: Any? = null
)

class EventHandleHelper{
    companion object{
        var logger = KotlinLogging.logger {}
        val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}

inline fun <reified T> EventHandleCommand.payload(): T? {
    if (this.data == null) return null

    return try {
        if (this.data is String) {
            EventHandleHelper.objectMapper.readValue<T>(this.data)
        } else {
            EventHandleHelper.objectMapper.convertValue<T>(this.data)
        }
    } catch (ex: Exception) {
        EventHandleHelper.logger.error() { "Unable to deserialize handle [${this.data}]: ${ex.message}" }
        null
    }
}