package cn.sunline.saas.dapr_wrapper.bindings

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import mu.KotlinLogging


data class BindingsCommand(val id: String? = null, val source: String? = null, val specversion: String?= null, val type: String?=null, val data: Any?=null)

class BindingsCommandHelper {
    companion object {
        var logger = KotlinLogging.logger {}
        val objectMapper: ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }
}

/**
 * BindingsCommand response type
 * Possible values are: SUCCESS, RETRY, DROP
 * See [Dapr Bindings Documentation](https://docs.dapr.io/reference/api/bindings_api/)
 */
enum class BindingsCommandResponseType {
    SUCCESS,
    RETRY,
    DROP
}

/**
 * BindingsCommand response object
 * @param[status] Status of the response, default to SUCCESS
 */
data class BindingsCommandResponse(val status: BindingsCommandResponseType = BindingsCommandResponseType.SUCCESS)

/**
 * Extension function to fetch Bindings event payload and deserialize into desired concrete class type
 */
inline fun <reified T> BindingsCommand.payload(): T? {
    if (this.data == null) return null

    return try {
        if (this.data is String) {
            BindingsCommandHelper.objectMapper.readValue<T>(this.data)
        } else {
            BindingsCommandHelper.objectMapper.convertValue<T>(this.data)
        }
    } catch (ex: Exception) {
        BindingsCommandHelper.logger.error() { "Unable to deserialize BindingsCommand [${this.data}]: ${ex.message}" }
        null
    }
}