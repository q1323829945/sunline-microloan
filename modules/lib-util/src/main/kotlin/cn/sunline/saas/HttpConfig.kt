package cn.sunline.saas

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.SystemException
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.global.constant.HttpRequestMethod.*
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.methods.*
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity
import org.apache.commons.httpclient.methods.multipart.PartBase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.InputStreamReader

@Component
class HttpConfig {
    protected val logger: Logger = LoggerFactory.getLogger(HttpConfig::class.java)

    fun getHttpMethod(httpMethod: HttpRequestMethod, uri:String, headerMap:Map<String,String>? = null, parts:Array<PartBase>?): HttpMethod {

        val httpRequest = getHttpMethod(httpMethod,uri,headerMap)

        when(httpRequest){
            is PutMethod -> httpRequest.requestEntity = MultipartRequestEntity(parts,httpRequest.params)
            is PostMethod -> httpRequest.requestEntity = MultipartRequestEntity(parts,httpRequest.params)
        }


        return httpRequest
    }

    fun getHttpMethod(httpMethod: HttpRequestMethod, uri:String, headerMap:Map<String,String>? = null, entity: RequestEntity? = null): HttpMethod {

        val httpRequest = getHttpMethod(httpMethod,uri,headerMap)

        when(httpRequest){
            is PutMethod -> httpRequest.requestEntity = entity
            is PostMethod -> httpRequest.requestEntity = entity
        }


        return httpRequest
    }

    private fun getHttpMethod(httpMethod: HttpRequestMethod, uri:String, headerMap:Map<String,String>? = null):HttpMethod{
        val httpRequest = when(httpMethod){
            GET -> GetMethod(uri)
            POST -> PostMethod(uri)
            PUT -> PutMethod(uri)
            DELETE -> DeleteMethod(uri)
        }

        headerMap?.forEach{
            httpRequest.addRequestHeader(it.key,it.value)
        }


        return httpRequest
    }

    fun sendClient(httpMethod: HttpMethod) {
        val httpClient = HttpClient()
        val status = httpClient.executeMethod(httpMethod)

        logger.debug("uri:${httpMethod.uri}")
        logger.debug("status:$status")
        if(!Regex("2[0-9]+").containsMatchIn(status.toString())){
            val body = getBody(httpMethod)
            logger.error("body:$body")
            throw SystemException("http error",ManagementExceptionCode.HTTP_ERROR)
        }
    }

    fun getHeader(httpMethod: HttpMethod):Map<String,String>{
        val map = HashMap<String,String>()
        httpMethod.responseHeaders.forEach {
            map[it.name] = it.value
        }
        return map
    }

    private fun getBody(httpMethod: HttpMethod):String{
        val inputStream = httpMethod.responseBodyAsStream

        val br = BufferedReader(InputStreamReader(inputStream))

        val strBuffer = StringBuffer()

        while (true){
            val str = br.readLine()?:break

            strBuffer.append(str)
        }

        return strBuffer.toString()
    }

    fun getResponseBody(httpMethod:HttpMethod):String{
        return getBody(httpMethod)
    }

}