package cn.sunline.saas.dapr_wrapper.pubsub

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging

/**
 * Data class for Cloud Event compatible envelop.
 * See [Cloud Event specification](https://github.com/cloudevents/spec)
 */
data class PubSubCommand(val id: String? = null, val source: String? = null, val specversion: String?= null, val type: String?=null, val data: Any?=null)

class PubSubCommandHelper {
    companion object {
        var logger = KotlinLogging.logger {}
        val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}

/**
 * PubSubCommand response type
 * Possible values are: SUCCESS, RETRY, DROP
 * See [Dapr PubSub Documentation](https://docs.dapr.io/reference/api/pubsub_api/)
 */
enum class PubSubCommandResponseType {
    SUCCESS,
    RETRY,
    DROP
}

/**
 * PubSubCommand response object
 * @param[status] Status of the response, default to SUCCESS
 */
data class PubSubCommandResponse(val status: PubSubCommandResponseType = PubSubCommandResponseType.SUCCESS)

/**
 * Extension function to fetch PubSub event payload and deserialize into desired concrete class type
 */
inline fun <reified T> PubSubCommand.payload(): T? {
    if (this.data == null) return null

    return try {
        if (this.data is String) {
            PubSubCommandHelper.objectMapper.readValue<T>(this.data)
        } else {
            PubSubCommandHelper.objectMapper.convertValue<T>(this.data)
        }
    } catch (ex: Exception) {
        PubSubCommandHelper.logger.error() { "Unable to deserialize PubSubCommand [${this.data}]: ${ex.message}" }
        null
    }
}