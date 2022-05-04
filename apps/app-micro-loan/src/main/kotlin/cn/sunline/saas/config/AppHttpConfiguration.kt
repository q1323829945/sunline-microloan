package cn.sunline.saas.config

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader

@Component
class AppHttpConfiguration {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


//    fun getPublicHeaders():Map<String,String>{
//        val map = mutableMapOf<String,String>()
//
//        map["ExternalTune"] = "customer-offer"
//        map["X-Tenant-Domain"] = ContextUtil.getTenant().toString()
//
//        return map
//    }
//
//    fun getResponse(httpMethod:HttpMethod):String{
//        val map = getBody(httpMethod)
//        if(map["code"].toString() != "0.0"){
//            val message = map["message"].toString()
//
//            logger.error("message:$message")
//            throw BusinessException(message,ManagementExceptionCode.HTTP_ERROR)
//        }
//
//        return objectMapper.valueToTree<JsonNode>(map["data"]).toPrettyString()
//    }
//
//
//    fun getBody(httpMethod:HttpMethod):Map<*,*>{
//        val inputStream = httpMethod.responseBodyAsStream
//
//        val br = BufferedReader(InputStreamReader(inputStream))
//
//        val strBuffer = StringBuffer()
//
//        while (true){
//            val str = br.readLine()?:break
//
//            strBuffer.append(str)
//        }
//
//        return objectMapper.convertValue<Map<*,*>>(strBuffer.toString())
//    }
}
