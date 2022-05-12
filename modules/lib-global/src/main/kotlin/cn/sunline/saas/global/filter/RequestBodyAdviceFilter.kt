package cn.sunline.saas.global.filter

import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setUserId
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.treeToValue
import org.apache.commons.io.IOUtils
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpInputMessage
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice
import java.io.InputStream
import java.lang.reflect.Type
import java.nio.charset.Charset

@ControllerAdvice
class RequestBodyAdviceFilter : RequestBodyAdvice {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    override fun supports(
        methodParameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): Boolean {
        return true
    }

    override fun beforeBodyRead(
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): HttpInputMessage {
        if(inputMessage.headers["traceparent"].isNullOrEmpty()){
            return inputMessage
        }

        val body = IOUtils.toString(inputMessage.body, Charset.forName("utf-8"))

        val bodyMap = objectMapper.treeToValue<Map<*,*>>(objectMapper.readTree(body))["data"] as Map<*, *>


        return object : HttpInputMessage{
            override fun getHeaders(): HttpHeaders {

                val httpHeaders = HttpHeaders()
                httpHeaders.setAll(inputMessage.headers.toSingleValueMap())

                bodyMap["metadata"]?.run {
                    this as Map<String,String>
                    httpHeaders.setAll(this)

                    this[Header.USER_AUTHORIZATION.key]?.run {
                        ContextUtil.setUserId(this)
                    }

                    this[Header.TENANT_AUTHORIZATION.key]?.run {
                        ContextUtil.setTenant(this)
                    }
                }

                return httpHeaders
            }

            override fun getBody(): InputStream {
                val data = bodyMap["data"]
                return if(data != null){
                    data as Map<*,*>
                    val dataJson = objectMapper.valueToTree<JsonNode>(data).toPrettyString()
                    IOUtils.toInputStream(dataJson,Charset.forName("utf-8"))
                } else {
                    IOUtils.toInputStream(body,Charset.forName("utf-8"))
                }

            }

        }
    }

    override fun afterBodyRead(
        body: Any,
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): Any {
        return body
    }

    override fun handleEmptyBody(
        body: Any?,
        inputMessage: HttpInputMessage,
        parameter: MethodParameter,
        targetType: Type,
        converterType: Class<out HttpMessageConverter<*>>
    ): Any? {
        return body
    }
}