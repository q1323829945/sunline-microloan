package cn.sunline.saas.config

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.global.constant.HttpRequestMethod.*
import cn.sunline.saas.multi_tenant.context.TenantContext
import com.google.gson.Gson
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.methods.*
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity
import org.apache.commons.httpclient.methods.multipart.PartBase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader

@Component
class HttpConfiguration {

    @Autowired
    private lateinit var tenantContext: TenantContext


    fun getHttpMethod(httpMethod: HttpRequestMethod, uri:String,parts:Array<PartBase>? = null): HttpMethod {


        val httpRequest = when(httpMethod){
            GET -> GetMethod(uri)
            POST -> PostMethod(uri)
            PUT -> PutMethod(uri)
            DELETE -> DeleteMethod(uri)
        }

        httpRequest.addRequestHeader("ExternalTune","customer-offer")
        httpRequest.addRequestHeader("X-Tenant-Domain",tenantContext.get().toString())

        if(parts != null && httpRequest is PostMethod){
            httpRequest.requestEntity = MultipartRequestEntity(parts,httpRequest.params)
        }

        return httpRequest
    }


    fun sendClient(httpMethod:HttpMethod) {
        val httpClient = HttpClient()
        val status = httpClient.executeMethod(httpMethod)

        if(status != 200){
            showErrorInfo(httpMethod)
        }
    }

    private fun showErrorInfo(httpMethod:HttpMethod){
        val map = getBody(httpMethod)
        throw BusinessException(map["message"].toString(),ManagementExceptionCode.HTTP_ERROR)

    }

    fun getResponse(httpMethod:HttpMethod):String{
        val map = getBody(httpMethod)
        if(map["code"].toString() != "0.0"){
            val message = map["message"].toString()
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
