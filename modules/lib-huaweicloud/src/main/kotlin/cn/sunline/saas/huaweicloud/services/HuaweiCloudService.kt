package cn.sunline.saas.huaweicloud.services

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.UploadException
import cn.sunline.saas.huaweicloud.config.*
import cn.sunline.saas.huaweicloud.models.HttpRequestMethod
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
        val uri = getUri(bucketName, huaweiCloudTools.region)

        //header
        val requestTime = huaweiCloudTools.getCloudUploadFormatDate()
        val canonicalizeResource = "/${bucketName}/"
        val signature = getSignature(HttpRequestMethod.DELETE,"","",requestTime,"",canonicalizeResource)
        val headerMap = mutableMapOf<String,String>()
        headerMap["Date"] = requestTime
        headerMap["Authorization"] = "OBS ${huaweiCloudTools.accessKey}:$signature"


        //httpDelete
        val httpDelete = getHttpMethod(HttpRequestMethod.DELETE,uri,headerMap)

        //sendClint
        sendClient(httpDelete)
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
            InputStreamRequestEntity(FileInputStream(body))
        }else if(body is InputStream){
            InputStreamRequestEntity(body)
        } else{
            throw UploadException(ManagementExceptionCode.BODY_TYPE_ERROR,"body error")
        }

        //httpPut
        val httpPut = getHttpMethod(HttpRequestMethod.PUT,uri,headerMap,entity)

        //sendClint
        sendClient(httpPut)
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
        val httpGet = getHttpMethod(HttpRequestMethod.GET,uri,headerMap)

        //sendClint
        sendClient(httpGet)

        return getResponseStream(httpGet)
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
        return  huaweiCloudTools.signWithHmacSha1(huaweiCloudTools.securityKey,sign)
    }

    fun getResponseStream(httpMethod:HttpMethod):InputStream{
        val inputStream = httpMethod.responseBodyAsStream
        val byteArrayOutputStream = ByteArrayOutputStream()
        val bytes = ByteArray(1024)
        var len = 0
        while (true){
            len = inputStream.read(bytes)

            if(len == -1){
                break
            }
            byteArrayOutputStream.write(bytes,0,len)
        }

        return ByteArrayInputStream(byteArrayOutputStream.toByteArray())
    }

    fun sendClient(httpMethod:HttpMethod) {
        val httpClient = HttpClient()
        val status = httpClient.executeMethod(httpMethod)

        logger.debug("status:$status")
        if(status != 200 && status != 204){
            throw UploadException(ManagementExceptionCode.FILE_UPLOAD_FAILED,"file upload error")
        }
    }


    fun getHttpMethod(httpMethod:HttpRequestMethod,uri:String,headerMap:Map<String,String>):HttpMethod{
        return getHttpMethod(httpMethod,uri,headerMap,null)
    }

    fun getHttpMethod(httpMethod:HttpRequestMethod,uri:String,headerMap:Map<String,String>,entity: RequestEntity?):HttpMethod{
        val httpRequest =  if(httpMethod == HttpRequestMethod.GET){
            GetMethod(uri)
        }else if(httpMethod == HttpRequestMethod.DELETE){
            DeleteMethod(uri)
        }else if (httpMethod == HttpRequestMethod.PUT){
            PutMethod(uri)
        }else if(httpMethod == HttpRequestMethod.POST){
            PostMethod(uri)
        }else{
            throw UploadException(ManagementExceptionCode.BODY_TYPE_ERROR)
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