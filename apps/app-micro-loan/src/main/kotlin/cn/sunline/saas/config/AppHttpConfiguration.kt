package cn.sunline.saas.config

import cn.sunline.saas.HttpConfig
import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import com.google.gson.Gson
import org.apache.commons.httpclient.HttpMethod
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader

@Component
class AppHttpConfiguration: HttpConfig() {

    fun getPublicHeaders():Map<String,String>{
        val map = mutableMapOf<String,String>()

        map["ExternalTune"] = "customer-offer"
        map["X-Tenant-Domain"] = ContextUtil.getTenant().toString()
        map["X-Authorization-Tenant"] = ContextUtil.getTenant().toString()
        return map
    }

    fun getResponse(httpMethod:HttpMethod):String{
        val map = getBody(httpMethod)
        if(map["code"].toString() != "0.0"){
            val message = map["message"].toString()

            logger.error("message:$message")
            throw BusinessException(message,ManagementExceptionCode.HTTP_ERROR)
        }

        return Gson().toJson(map["data"])
    }


    fun getBody(httpMethod:HttpMethod):Map<*,*>{
        val inputStream = httpMethod.responseBodyAsStream

        val br = BufferedReader(InputStreamReader(inputStream))

        val strBuffer = StringBuffer()

        while (true){
            val str = br.readLine()?:break

            strBuffer.append(str)
        }

        return Gson().fromJson(strBuffer.toString(),Map::class.java)
    }
}
