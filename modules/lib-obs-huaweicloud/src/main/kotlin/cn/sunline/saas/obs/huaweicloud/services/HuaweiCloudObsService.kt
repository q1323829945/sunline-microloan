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
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.InputStream
import java.net.URLEncoder
import java.nio.charset.Charset

@Service
class HuaweiCloudObsService:ObsApi {

    @Autowired
    private lateinit var huaweiCloudConfig: HuaweiCloudConfig

    @Autowired
    private lateinit var httpConfig: HttpConfig

    data class Picture(
        val key:String,
        val url:String,
        val expires:Long
    )

    val pictures = mutableMapOf<String,Picture>()

    /**
     * https://support.huaweicloud.com/api-obs/obs_04_0021.html
     */
    override fun createBucket(bucketParams: BucketParams) {
        val regin = bucketParams.createBucketConfiguration.locationConstraint
        //uri
        val uri = getUri(bucketParams.bucketName, regin)

        //header
        val headers = initHeader(HttpRequestMethod.PUT,bucketParams.bucketName,null,"application/xml")
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
        val headers = initHeader(HttpRequestMethod.PUT,bucketName)

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
        val headers = initHeader(HttpRequestMethod.PUT,key = key)

        //body
        val entity = when(val body = putParams.body){
            is String -> httpConfig.setRequestBody(body.toByteArray())
            is InputStream -> {
                val entity = httpConfig.setRequestBody(body.readBytes())
                body.close()
                entity
            }
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
        val headers = initHeader(HttpRequestMethod.GET,key = key)


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
        val headers = initHeader(HttpRequestMethod.DELETE,key = key)

        httpConfig.execute(HttpRequestMethod.DELETE,uri,null,headers)
    }

    /**
     * https://support.huaweicloud.com/intl/zh-cn/usermanual-obs/obs_03_0046.html
     */
    override fun getPictureView(getParams: GetParams): String {
        val picture = pictures[getParams.key]
        picture?.run {
            if(System.currentTimeMillis() / 1000 + 60 < this.expires){
                return this.url
            }
        }

        val key = URLEncoder.encode(getParams.key,"utf-8")
        val basePath = getUri(huaweiCloudConfig.bucketName, huaweiCloudConfig.region, key)

        val expires = getExpires()
        val canonicalizeResource = getPictureViewCanonicalizeResource(key)
        val signature = getSignature(HttpRequestMethod.GET,"","",expires.toString(),"",canonicalizeResource)
        val urlEncoderSignature = URLEncoder.encode(signature, Charset.defaultCharset())
        val url = "$basePath?Signature=$urlEncoderSignature&Expires=$expires&AccessKeyId=${huaweiCloudConfig.accessKey}&${getPictureUrlQuery()}"

        pictures[getParams.key] = Picture(getParams.key,url,expires)

        return url
    }

    private fun getPictureViewCanonicalizeResource(key:String):String{
        val canonicalizeResource = "/${huaweiCloudConfig.bucketName}/$key?"
        val path = getPictureUrlQuery()
        return canonicalizeResource + path
    }

    private fun getPictureUrlQuery():String{
        val params = mutableListOf("response-content-disposition=inline",
//            "response-content-type=application/octet-stream",
//            "versionId=null",
            "x-image-process=style/${huaweiCloudConfig.style}"
        )

        val query = StringBuilder()
        for(i in params.indices){
            query.append(params[i])
            if(i < params.size - 1){
                query.append("&")
            }
        }

        return query.toString()
    }

    private fun getExpires(): Long {
        return System.currentTimeMillis() / 1000 + 3600
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

    private fun initHeader(httpRequestMethod: HttpRequestMethod, bucketName: String = huaweiCloudConfig.bucketName, key: String? = null, contentType: String = ""):MutableMap<String,String>{
        val requestTime = huaweiCloudConfig.getCloudUploadFormatDate()
        val canonicalizeResource = key?.run { "/$bucketName/$key" }?:run { "/$bucketName/" }
        val signature = getSignature(httpRequestMethod,"",contentType,requestTime,"",canonicalizeResource)
        val headers = mutableMapOf<String,String>()
        headers["Date"] = requestTime
        headers["Authorization"] = "OBS ${huaweiCloudConfig.accessKey}:$signature"

        return headers
    }

}
