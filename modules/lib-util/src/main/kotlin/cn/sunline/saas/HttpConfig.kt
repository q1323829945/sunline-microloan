package cn.sunline.saas

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.SystemException
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.global.constant.HttpRequestMethod.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

@Component
class HttpConfig {
    protected val logger: Logger = LoggerFactory.getLogger(HttpConfig::class.java)



    fun execute(httpMethod: HttpRequestMethod, uri:String, parts:List<MultipartBody.Part>, headers:Map<String,String>? = null): Response {
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
        parts.forEach {
            multipartBody.addPart(it)
        }

        return execute(httpMethod,uri,multipartBody.build(),headers)
    }



    fun execute(httpMethod: HttpRequestMethod, uri:String, requestBody: RequestBody? = null,headers:Map<String,String>? = null):Response{
        val client = OkHttpClient.Builder().build()
        val request = when(httpMethod){
            GET -> Request.Builder().url(uri).get()
            POST -> Request.Builder().url(uri).post(requestBody!!)
            PUT -> Request.Builder().url(uri).put(requestBody!!)
            DELETE -> Request.Builder().url(uri).delete(requestBody)
        }

        headers?.forEach {
            request.header(it.key,it.value)
        }

        val response = client.newCall(request.build()).execute()

        logger.debug("uri:${response.request.url}")
        logger.debug("code:${response.code}")
        if(!response.isSuccessful){
            val body = getBody(response)
            logger.error("body:$body")
            throw SystemException("http error",ManagementExceptionCode.HTTP_ERROR)
        }

        return response
    }

    fun setRequestBody(bytes:ByteArray,mediaType: String? = null):RequestBody{
        return bytes.toRequestBody(mediaType?.toMediaType())
    }

    fun setRequestBody(str:String,mediaType: String? = null):RequestBody{
        return str.toRequestBody(mediaType?.toMediaType())
    }

    fun setRequestBody(file: File,mediaType: String? = null):RequestBody{
        return file.asRequestBody(mediaType?.toMediaType())
    }


    fun getHeader(response: Response):Map<String,String>{
        val map = HashMap<String,String>()
        response.headers.forEach {
            map[it.first] = it.second
        }
        return map
    }

    private fun getBody(response: Response): String {
        val stream = response.body?.byteStream()
        return IOUtils.toString(stream, Charset.defaultCharset())
    }

    fun getResponseBody(response: Response):String{
        return getBody(response)
    }

    fun getResponseStream(response: Response): InputStream {
        return response.body!!.byteStream()
    }

}