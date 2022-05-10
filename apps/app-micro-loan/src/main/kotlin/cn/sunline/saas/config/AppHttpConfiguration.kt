package cn.sunline.saas.config

import cn.sunline.saas.HttpConfig
import org.springframework.stereotype.Component

@Component
class AppHttpConfiguration: HttpConfig() {

//    fun getPublicHeaders():Map<String,String>{
//        val map = mutableMapOf<String,String>()
//
//        map["ExternalTune"] = "customer-offer"
//        map["X-Tenant-Domain"] = ContextUtil.getTenant().toString()
//        map["X-Authorization-Tenant"] = ContextUtil.getTenant().toString()
//        return map
//    }
//
//    fun getResponse(httpMethod: HttpMethod):String{
//        val map = getBody(httpMethod)
//        if(map["code"].toString() != "0.0"){
//            val message = map["message"].toString()
//
//            logger.error("message:$message")
//            throw BusinessException(message,ManagementExceptionCode.HTTP_ERROR)
//        }
//
//        return Gson().toJson(map["data"])
//    }
//
//
//    fun getBody(httpMethod: HttpMethod):Map<*,*>{
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
//        return Gson().fromJson(strBuffer.toString(),Map::class.java)
//    }
}
