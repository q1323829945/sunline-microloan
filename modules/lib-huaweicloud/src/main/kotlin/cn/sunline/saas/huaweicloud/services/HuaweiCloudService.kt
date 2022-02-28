package cn.sunline.saas.huaweicloud.services

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.UploadException
import cn.sunline.saas.huaweicloud.config.*
import cn.sunline.saas.huaweicloud.models.HttpRequestMethod
import cn.sunline.saas.obs.api.*
import org.apache.http.HttpEntity
import org.apache.http.client.methods.*
import org.apache.http.entity.InputStreamEntity
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import java.io.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths


@Service
class HuaweiCloudService:ObsApi {

    @Autowired
    private lateinit var huaweiCloudTools: HuaweiCloudTools

    private val logger = LoggerFactory.getLogger(HuaweiCloudService::class.java)

    override fun createBucket(bucketParams: BucketParams) {
        val regin = bucketParams.createBucketConfiguration.locationConstraint
        //uri
        val uri = getUri(bucketParams.bucketName, regin)

        //header
        val requestTime = huaweiCloudTools.getCloudUploadFormatDate()
        val canonicalizeResource = "/${bucketParams.bucketName}/"
        val signature = getSignature(HttpRequestMethod.PUT,"","application/xml",requestTime,"",canonicalizeResource)
        val headerMap = mutableMapOf<String,String>()
        headerMap["Date"] = requestTime
        headerMap["Authorization"] = "OBS ${huaweiCloudTools.accessKey}:$signature"
        headerMap["Content-Type"] = "application/xml"

        //body
        val date = DateTime.now().toString("yyyy-MM-dd")

        val createBucketTemplate = "<CreateBucketConfiguration " +
                "xmlns=\"http://$regin.myhuaweicloud.com/doc/$date/\">\n" +
                "<Location>" + regin + "</Location>\n" +
                "</CreateBucketConfiguration>"
        val body = StringEntity(createBucketTemplate)

        //httpPut
        val httpPut = getHttpRequest(HttpRequestMethod.PUT,uri,headerMap,body)

        //response
        val response = setHttpClientAndGetResult(httpPut)

        huaweiCloudResponseLog(response,httpPut)
    }


    override fun putBucketLifecycleConfiguration(lifecycleParams: LifecycleParams) {
        TODO("Not yet implemented")
    }

    override fun deleteBucket(bucketName: String) {
        //uri
        val uri = getUri(bucketName, huaweiCloudTools.region)

        //header
        val requestTime = huaweiCloudTools.getCloudUploadFormatDate()
        val canonicalizeResource = "/${bucketName}/"
        val signature = getSignature(HttpRequestMethod.DELETE,"","",requestTime,"",canonicalizeResource)
        val headerMap = mutableMapOf<String,String>()
        headerMap["Date"] = requestTime
        headerMap["Authorization"] = "OBS ${huaweiCloudTools.accessKey}:$signature"


        //httpDelete
        val httpDelete = getHttpRequest(HttpRequestMethod.DELETE,uri,headerMap,null)

        //response
        val response = setHttpClientAndGetResult(httpDelete)

        huaweiCloudResponseLog(response,httpDelete)

    }

    override fun putObject(putParams: PutParams) {

        //uri
        val uri = getUri(putParams.bucketName, huaweiCloudTools.region,putParams.key)


        //header
        val requestTime = huaweiCloudTools.getCloudUploadFormatDate()
        val canonicalizeResource = "/${putParams.bucketName}/${putParams.key}"
        val signature = getSignature(HttpRequestMethod.PUT,"","",requestTime,"",canonicalizeResource)
        val headerMap = mutableMapOf<String,String>()
        headerMap["Date"] = requestTime
        headerMap["Authorization"] = "OBS ${huaweiCloudTools.accessKey}:$signature"

        //body
        val body = putParams.body
        val entity = if(body is String){
            InputStreamEntity(FileInputStream(body))
        }else if(body is InputStream){
            InputStreamEntity(body)
        } else{
            throw UploadException(ManagementExceptionCode.BODY_TYPE_ERROR,"body error")
        }

        //httpPut
        val httpPut = getHttpRequest(HttpRequestMethod.PUT,uri,headerMap,entity)

        //response
        val response = setHttpClientAndGetResult(httpPut)

        huaweiCloudResponseLog(response,httpPut)
    }

    override fun getObject(getParams: GetParams): Any? {
        //uri
        val uri = getUri(getParams.bucketName, huaweiCloudTools.region,getParams.key)

        //header
        val requestTime = huaweiCloudTools.getCloudUploadFormatDate()
        val canonicalizeResource = "/${getParams.bucketName}/${getParams.key}"
        val signature = getSignature(HttpRequestMethod.GET,"","",requestTime,"",canonicalizeResource)
        val headerMap = mutableMapOf<String,String>()
        headerMap["Date"] = requestTime
        headerMap["Authorization"] = "OBS ${huaweiCloudTools.accessKey}:$signature"


        //httpGet
        val httpGet = getHttpRequest(HttpRequestMethod.GET,uri,headerMap,null)

        //response
        val response = setHttpClientAndGetResult(httpGet)

//        huaweiCloudResponseLog(response,httpGet)
        //TODO: outputSteam throw error:stock closed
        return null
    }

    override fun deleteObject(deleteParams: DeleteParams) {
        //uri
        val uri = getUri(deleteParams.bucketName, huaweiCloudTools.region,deleteParams.key)

        //header
        val requestTime = huaweiCloudTools.getCloudUploadFormatDate()
        val canonicalizeResource = "/${deleteParams.bucketName}/${deleteParams.key}"
        val signature = getSignature(HttpRequestMethod.DELETE,"","",requestTime,"",canonicalizeResource)
        val headerMap = mutableMapOf<String,String>()
        headerMap["Date"] = requestTime
        headerMap["Authorization"] = "OBS ${huaweiCloudTools.accessKey}:$signature"

        //httpDelete
        val httpDelete = getHttpRequest(HttpRequestMethod.DELETE,uri,headerMap,null)

        //response
        val response = setHttpClientAndGetResult(httpDelete)

        huaweiCloudResponseLog(response,httpDelete)
    }

    fun getSignature(requestMode:HttpRequestMethod,md5:String,contentType:String,requestTime:String,canonicalizeHeaders:String,canonicalizeResource:String):String{
        val sign = "${requestMode.name}\n" +
                    "$md5\n" +
                    "$contentType\n" +
                    "$requestTime\n" +
                    "$canonicalizeHeaders$canonicalizeResource"
        return  huaweiCloudTools.signWithHmacSha1(huaweiCloudTools.securityKey,sign)
    }

    fun setHttpClientAndGetResult(httpRequestBase: HttpRequestBase):CloseableHttpResponse{
        val httpClient = HttpClients.createDefault()
        val httpResponse = httpClient.execute(httpRequestBase)
        httpClient.close()
        return httpResponse
    }

    fun getHttpRequest(httpMethod:HttpRequestMethod,uri:String,headerMap:Map<String,String>,entity: HttpEntity?):HttpRequestBase{
        val httpRequest =  if(httpMethod == HttpRequestMethod.GET){
            HttpGet(uri)
        }else if(httpMethod == HttpRequestMethod.DELETE){
            HttpDelete(uri)
        }else if (httpMethod == HttpRequestMethod.PUT){
            HttpPut(uri)

        }else if(httpMethod == HttpRequestMethod.POST){
            HttpPost(uri)
        }else{
            throw UploadException(ManagementExceptionCode.BODY_TYPE_ERROR)
        }
        headerMap.forEach{
            httpRequest.addHeader(it.key,it.value)
        }

        if(httpRequest is HttpPut){
            httpRequest.entity = entity
        }

        return httpRequest
    }



    fun huaweiCloudResponseLog(httpResponse:CloseableHttpResponse,httpRequestBase: HttpRequestBase){

//        logger.debug("Request Message:")

        println("${httpRequestBase.requestLine}")

        for(header in httpRequestBase.allHeaders){
            println("${header.name} : ${header.value}")
        }

        if(httpRequestBase is HttpPut){
            println("${httpRequestBase.entity.content}")

        }

        println("Response Message:")

        println("${httpResponse.statusLine}")

        for(header in httpResponse.allHeaders){
            println("${header.name} : ${header.value}")
        }

        if(httpResponse.entity != null){
            val reader = BufferedReader(InputStreamReader(httpResponse.entity.content))
            val str = StringBuilder()

            while (true){
                val line = reader.readLine()?:break
                str.append(line)
            }

            println("这个是打印结果：$str")
        }


        if(httpResponse.statusLine.statusCode != 200 && httpResponse.statusLine.statusCode != 204){
            throw UploadException(ManagementExceptionCode.FILE_UPLOAD_FAILED,"file upload error")
        }


    }

    fun getUri(bucketName: String,region:String,key:String):String{
        return "http://$bucketName.obs.$region.myhuaweicloud.com/$key"
    }

    fun getUri(bucketName: String,region:String):String{
        return "http://$bucketName.obs.$region.myhuaweicloud.com"
    }
}