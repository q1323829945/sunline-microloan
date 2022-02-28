package cn.sunline.saas.huaweicloud.services

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.UploadException
import cn.sunline.saas.huaweicloud.config.*
import cn.sunline.saas.obs.api.*
import org.apache.http.HttpEntity
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.InputStreamEntity
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.HttpClients
import org.joda.time.DateTime
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder


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
        val signature = getSignature("PUT","","application/xml",requestTime,"",canonicalizeResource)
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
        val httpPut = getHttpPut(uri,headerMap,body)

        //response
        val response = setHttpClientAndGetResult(httpPut)

        huaweiCloudResponseLog(response,httpPut)
    }


    override fun putBucketLifecycleConfiguration(lifecycleParams: LifecycleParams) {
        TODO("Not yet implemented")
    }

    override fun deleteBucket(bucketName: String) {
    }

    override fun putObject(putParams: PutParams) {

        //uri
        val uri = getUri(putParams.bucketName, huaweiCloudTools.region,putParams.key)


        //header
        val requestTime = huaweiCloudTools.getCloudUploadFormatDate()
        val canonicalizeResource = "/${putParams.bucketName}/${putParams.key}"
        val signature = getSignature("PUT","","",requestTime,"",canonicalizeResource)
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
        val httpPut = getHttpPut(uri,headerMap,entity)

        //response
        val response = setHttpClientAndGetResult(httpPut)

        huaweiCloudResponseLog(response,httpPut)
    }

    override fun getObject(getParams: GetParams): Any? {
        TODO("Not yet implemented")
    }

    override fun deleteObject(deleteParams: DeleteParams) {
        TODO("Not yet implemented")
    }

    fun getSignature(requestMode:String,md5:String,contentType:String,requestTime:String,canonicalizeHeaders:String,canonicalizeResource:String):String{
        val sign = "$requestMode\n" +
                    "$md5\n" +
                    "$contentType\n" +
                    "$requestTime\n" +
                    "$canonicalizeHeaders$canonicalizeResource"
        return  huaweiCloudTools.signWithHmacSha1(huaweiCloudTools.securityKey,sign)
    }

    fun setHttpClientAndGetResult(httpPut: HttpPut):CloseableHttpResponse{
        val httpClient = HttpClients.createDefault()
        val httpResponse = httpClient.execute(httpPut)
        httpClient.close()
        return httpResponse
    }

    fun getHttpPut(uri:String,headerMap:Map<String,String>,entity: HttpEntity):HttpPut{
        val httpPut = HttpPut(uri)

        headerMap.forEach{
            httpPut.addHeader(it.key,it.value)
        }

        httpPut.entity = entity

        return httpPut
    }

    fun huaweiCloudResponseLog(httpResponse:CloseableHttpResponse,httpPut: HttpPut){

        logger.debug("Request Message:")
        logger.debug("${httpPut.requestLine}")

        for(header in httpPut.allHeaders){
            logger.debug("${header.name} : ${header.value}")
        }

        logger.debug("${httpPut.entity.content}")


        logger.debug("Response Message:")

        logger.debug("${httpResponse.statusLine}")

        for(header in httpResponse.allHeaders){
            logger.debug("${header.name} : ${header.value}")
        }

        val reader = BufferedReader(InputStreamReader(httpResponse.entity.content))
        val str = StringBuilder()

        while (true){
            val line = reader.readLine()?:break
            str.append(line)
        }

        logger.debug(str.toString())

        if(httpResponse.statusLine.statusCode != 200){
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