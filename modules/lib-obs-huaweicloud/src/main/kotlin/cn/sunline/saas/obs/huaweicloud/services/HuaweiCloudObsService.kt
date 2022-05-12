package cn.sunline.saas.obs.huaweicloud.services

import cn.sunline.saas.HttpConfig
import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.obs.huaweicloud.config.HuaweiCloudConfig
import cn.sunline.saas.obs.huaweicloud.exception.ObsBodyTypeException
import cn.sunline.saas.obs.api.*
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.io.InputStream
import java.net.URLEncoder

@Service
class HuaweiCloudObsService:ObsApi {

    @Autowired
    private lateinit var huaweiCloudConfig: HuaweiCloudConfig

    @Autowired
    private lateinit var httpConfig: HttpConfig

    /**
     * https://support.huaweicloud.com/api-obs/obs_04_0021.html
     */
    override fun createBucket(bucketParams: BucketParams) {
        val regin = bucketParams.createBucketConfiguration.locationConstraint
        //uri
        val uri = getUri(bucketParams.bucketName, regin)

        //header
        val requestTime = huaweiCloudConfig.getCloudUploadFormatDate()
        val canonicalizeResource = "/${bucketParams.bucketName}/"
        val signature = getSignature(HttpRequestMethod.PUT,"","application/xml",requestTime,"",canonicalizeResource)
        val headers = mutableMapOf<String,String>()
        headers["Date"] = requestTime
        headers["Authorization"] = "OBS ${huaweiCloudConfig.accessKey}:$signature"
        headers["Content-Type"] = "application/xml"

        //body
        val date = DateTime.now().toString("yyyy-MM-dd")

        val createBucketTemplate = "<CreateBucketConfiguration " +
                "xmlns=\"http://$regin.myhuaweicloud.com/doc/$date/\">\n" +
                "<Location>" + regin + "</Location>\n" +
                "</CreateBucketConfiguration>"

        httpConfig.execute(HttpRequestMethod.PUT,uri,httpConfig.setRequestBody(createBucketTemplate.toByteArray()),headers)
    }


    /**
     * https://support.huaweicloud.com/api-obs/obs_04_0034.html
     */
    override fun putBucketLifecycleConfiguration(lifecycleParams: LifecycleParams) {
        TODO("Not yet implemented")
    }

    /**
     * https://support.huaweicloud.com/api-obs/obs_04_0025.html
     */
    override fun deleteBucket(bucketName: String) {
        //uri
        val uri = getUri(bucketName, huaweiCloudConfig.region)

        //header
        val requestTime = huaweiCloudConfig.getCloudUploadFormatDate()
        val canonicalizeResource = "/${bucketName}/"
        val signature = getSignature(HttpRequestMethod.DELETE,"","",requestTime,"",canonicalizeResource)
        val headers = mutableMapOf<String,String>()
        headers["Date"] = requestTime
        headers["Authorization"] = "OBS ${huaweiCloudConfig.accessKey}:$signature"

        httpConfig.execute(HttpRequestMethod.DELETE,uri,null,headers)
    }

    /**
     * https://support.huaweicloud.com/api-obs/obs_04_0080.html
     */
    override fun putObject(putParams: PutParams) {

        //uri
        val key = URLEncoder.encode(putParams.key,"utf-8")
        val uri = getUri(huaweiCloudConfig.bucketName, huaweiCloudConfig.region, key)

        //header
        val requestTime = huaweiCloudConfig.getCloudUploadFormatDate()
        val canonicalizeResource = "/${huaweiCloudConfig.bucketName}/$key"
        val signature = getSignature(HttpRequestMethod.PUT,"","",requestTime,"",canonicalizeResource)
        val headers = mutableMapOf<String,String>()
        headers["Date"] = requestTime
        headers["Authorization"] = "OBS ${huaweiCloudConfig.accessKey}:$signature"

        //body
        val entity = when(val body = putParams.body){
            is String -> httpConfig.setRequestBody(FileInputStream(body).readBytes())
            is InputStream -> httpConfig.setRequestBody(body.readBytes())
            is ByteArray -> httpConfig.setRequestBody(body)
            else -> throw ObsBodyTypeException("body type error", ManagementExceptionCode.BODY_TYPE_ERROR)
        }

        httpConfig.execute(HttpRequestMethod.PUT,uri,entity,headers)
    }

    /**
     * https://support.huaweicloud.com/api-obs/obs_04_0083.html
     */
    override fun getObject(getParams: GetParams): Any? {

        //uri
        val key = URLEncoder.encode(getParams.key,"utf-8")
        val uri = getUri(huaweiCloudConfig.bucketName, huaweiCloudConfig.region, key)
        //header
        val requestTime = huaweiCloudConfig.getCloudUploadFormatDate()
        val canonicalizeResource = "/${huaweiCloudConfig.bucketName}/$key"
        val signature = getSignature(HttpRequestMethod.GET,"","",requestTime,"",canonicalizeResource)
        val headers = mutableMapOf<String,String>()
        headers["Date"] = requestTime
        headers["Authorization"] = "OBS ${huaweiCloudConfig.accessKey}:$signature"

        val response = httpConfig.execute(HttpRequestMethod.GET,uri,null,headers)

        return httpConfig.getResponseStream(response)
    }

    /**
     * https://support.huaweicloud.com/api-obs/obs_04_0085.html
     */
    override fun deleteObject(deleteParams: DeleteParams) {

        //uri
        val key = URLEncoder.encode(deleteParams.key,"utf-8")
        val uri = getUri(huaweiCloudConfig.bucketName, huaweiCloudConfig.region, key)
        //header
        val requestTime = huaweiCloudConfig.getCloudUploadFormatDate()
        val canonicalizeResource = "/${huaweiCloudConfig.bucketName}/$key"
        val signature = getSignature(HttpRequestMethod.DELETE,"","",requestTime,"",canonicalizeResource)
        val headers = mutableMapOf<String,String>()
        headers["Date"] = requestTime
        headers["Authorization"] = "OBS ${huaweiCloudConfig.accessKey}:$signature"

        httpConfig.execute(HttpRequestMethod.DELETE,uri,null,headers)
    }

    private fun getSignature(requestMode:HttpRequestMethod,md5:String,contentType:String,requestTime:String,canonicalizeHeaders:String,canonicalizeResource:String):String{
        val sign = "${requestMode.name}\n" +
                "$md5\n" +
                "$contentType\n" +
                "$requestTime\n" +
                "$canonicalizeHeaders$canonicalizeResource"
        return huaweiCloudConfig.signWithHmacSha1(huaweiCloudConfig.securityKey, sign)
    }


    private fun getUri(bucketName: String,region:String,key:String):String{
        return "http://$bucketName.obs.$region.myhuaweicloud.com/$key"
    }

    private fun getUri(bucketName: String,region:String):String{
        return "http://$bucketName.obs.$region.myhuaweicloud.com"
    }

}
