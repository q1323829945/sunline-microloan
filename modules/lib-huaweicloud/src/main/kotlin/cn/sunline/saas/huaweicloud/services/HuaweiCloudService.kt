package cn.sunline.saas.huaweicloud.services

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.SystemException
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.huaweicloud.config.HuaweiCloudConfig
import cn.sunline.saas.huaweicloud.exception.ObsBodyTypeException
import cn.sunline.saas.obs.api.*
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.HttpMethod
import org.apache.commons.httpclient.methods.*
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.*

@Service
class HuaweiCloudService:ObsApi {

    @Autowired
    private lateinit var huaweiCloudConfig: HuaweiCloudConfig

    private val logger = LoggerFactory.getLogger(HuaweiCloudService::class.java)

    override fun createBucket(bucketParams: BucketParams) {
        val regin = bucketParams.createBucketConfiguration.locationConstraint
        //uri
        val uri = getUri(bucketParams.bucketName, regin)

        //header
        val requestTime = huaweiCloudConfig.getCloudUploadFormatDate()
        val canonicalizeResource = "/${bucketParams.bucketName}/"
        val signature = getSignature(HttpRequestMethod.PUT,"","application/xml",requestTime,"",canonicalizeResource)
        val headerMap = mutableMapOf<String,String>()
        headerMap["Date"] = requestTime
        headerMap["Authorization"] = "OBS ${huaweiCloudConfig.accessKey}:$signature"
        headerMap["Content-Type"] = "application/xml"

        //body
        val date = DateTime.now().toString("yyyy-MM-dd")

        val createBucketTemplate = "<CreateBucketConfiguration " +
                "xmlns=\"http://$regin.myhuaweicloud.com/doc/$date/\">\n" +
                "<Location>" + regin + "</Location>\n" +
                "</CreateBucketConfiguration>"

        val inputStream = ByteArrayInputStream(createBucketTemplate.toByteArray())

        val body = InputStreamRequestEntity(inputStream)

        //httpPut
        val httpPut = getHttpMethod(HttpRequestMethod.PUT,uri,headerMap,body)

        //sendClint
        sendClient(httpPut)
    }


    override fun putBucketLifecycleConfiguration(lifecycleParams: LifecycleParams) {
        TODO("Not yet implemented")
    }

    override fun deleteBucket(bucketName: String) {
        //uri
        val uri = getUri(bucketName, huaweiCloudConfig.region)

        //header
        val requestTime = huaweiCloudConfig.getCloudUploadFormatDate()
        val canonicalizeResource = "/${bucketName}/"
        val signature = getSignature(HttpRequestMethod.DELETE,"","",requestTime,"",canonicalizeResource)
        val headerMap = mutableMapOf<String,String>()
        headerMap["Date"] = requestTime
        headerMap["Authorization"] = "OBS ${huaweiCloudConfig.accessKey}:$signature"


        //httpDelete
        val httpDelete = getHttpMethod(HttpRequestMethod.DELETE,uri,headerMap)

        //sendClint
        sendClient(httpDelete)
    }

    override fun putObject(putParams: PutParams) {

        //uri
        val uri = getUri(huaweiCloudConfig.bucketName, huaweiCloudConfig.region, putParams.key)

        //header
        val requestTime = huaweiCloudConfig.getCloudUploadFormatDate()
        val canonicalizeResource = "/${huaweiCloudConfig.bucketName}/${putParams.key}"
        val signature = getSignature(HttpRequestMethod.PUT,"","",requestTime,"",canonicalizeResource)
        val headerMap = mutableMapOf<String,String>()
        headerMap["Date"] = requestTime
        headerMap["Authorization"] = "OBS ${huaweiCloudConfig.accessKey}:$signature"

        //body
        val entity = when(val body = putParams.body){
            is String -> InputStreamRequestEntity(FileInputStream(body))
            is InputStream -> InputStreamRequestEntity(body)
            else -> throw ObsBodyTypeException("body type error", ManagementExceptionCode.BODY_TYPE_ERROR)
        }

        //httpPut
        val httpPut = getHttpMethod(HttpRequestMethod.PUT,uri,headerMap,entity)

        //sendClint
        sendClient(httpPut)
    }

    override fun getObject(getParams: GetParams): Any? {
        //uri
        val uri = getUri(huaweiCloudConfig.bucketName, huaweiCloudConfig.region, getParams.key)
        //header
        val requestTime = huaweiCloudConfig.getCloudUploadFormatDate()
        val canonicalizeResource = "/${huaweiCloudConfig.bucketName}/${getParams.key}"
        val signature = getSignature(HttpRequestMethod.GET,"","",requestTime,"",canonicalizeResource)
        val headerMap = mutableMapOf<String,String>()
        headerMap["Date"] = requestTime
        headerMap["Authorization"] = "OBS ${huaweiCloudConfig.accessKey}:$signature"


        //httpGet
        val httpGet = getHttpMethod(HttpRequestMethod.GET,uri,headerMap)

        //sendClint
        sendClient(httpGet)

        return httpGet.responseBodyAsStream
    }

    override fun deleteObject(deleteParams: DeleteParams) {
        //uri
        val uri = getUri(huaweiCloudConfig.bucketName, huaweiCloudConfig.region, deleteParams.key)
        //header
        val requestTime = huaweiCloudConfig.getCloudUploadFormatDate()
        val canonicalizeResource = "/${huaweiCloudConfig.bucketName}/${deleteParams.key}"
        val signature = getSignature(HttpRequestMethod.DELETE,"","",requestTime,"",canonicalizeResource)
        val headerMap = mutableMapOf<String,String>()
        headerMap["Date"] = requestTime
        headerMap["Authorization"] = "OBS ${huaweiCloudConfig.accessKey}:$signature"

        //httpDelete
        val httpDelete = getHttpMethod(HttpRequestMethod.DELETE,uri,headerMap)

        //sendClint
        sendClient(httpDelete)
    }


    fun getSignature(requestMode:HttpRequestMethod,md5:String,contentType:String,requestTime:String,canonicalizeHeaders:String,canonicalizeResource:String):String{
        val sign = "${requestMode.name}\n" +
                "$md5\n" +
                "$contentType\n" +
                "$requestTime\n" +
                "$canonicalizeHeaders$canonicalizeResource"
        return huaweiCloudConfig.signWithHmacSha1(huaweiCloudConfig.securityKey, sign)
    }

    fun sendClient(httpMethod:HttpMethod) {
        val httpClient = HttpClient()
        val status = httpClient.executeMethod(httpMethod)

        logger.debug("uri:${httpMethod.uri}")
        logger.debug("status:$status")
        if(status != 200 && status != 204){
            getBody(httpMethod)
            throw SystemException("http error",ManagementExceptionCode.BODY_TYPE_ERROR)
        }
    }

    fun getBody(httpMethod:HttpMethod){
        val inputStream = httpMethod.responseBodyAsStream

        val br = BufferedReader(InputStreamReader(inputStream))

        val strBuffer = StringBuffer()

        while (true){
            val str = br.readLine()?:break

            strBuffer.append(str)
        }

        logger.debug("body:$strBuffer")
    }


    fun getHttpMethod(httpMethod: HttpRequestMethod, uri:String, headerMap:Map<String,String>, entity: RequestEntity? = null):HttpMethod{
        val httpRequest = when(httpMethod){
            HttpRequestMethod.GET -> GetMethod(uri)
            HttpRequestMethod.POST -> PostMethod(uri)
            HttpRequestMethod.PUT -> PutMethod(uri)
            HttpRequestMethod.DELETE -> DeleteMethod(uri)
        }

        headerMap.forEach{
            httpRequest.addRequestHeader(it.key,it.value)
        }

        if(httpRequest is PutMethod){
            httpRequest.requestEntity = entity
        }

        return httpRequest
    }

    fun getUri(bucketName: String,region:String,key:String):String{
        return "http://$bucketName.obs.$region.myhuaweicloud.com/$key"
    }

    fun getUri(bucketName: String,region:String):String{
        return "http://$bucketName.obs.$region.myhuaweicloud.com"
    }
}