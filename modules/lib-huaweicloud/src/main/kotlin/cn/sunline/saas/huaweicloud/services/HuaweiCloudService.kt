package cn.sunline.saas.huaweicloud.services

import cn.sunline.saas.exceptions.ManagementExceptionCode
import cn.sunline.saas.exceptions.UploadException
import cn.sunline.saas.huaweicloud.config.ACCESS_KEY
import cn.sunline.saas.huaweicloud.config.HuaweiCloudTools
import cn.sunline.saas.huaweicloud.config.REGION
import cn.sunline.saas.huaweicloud.config.SECURITY_KEY
import cn.sunline.saas.obs.api.*
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.InputStreamEntity
import org.apache.http.impl.client.HttpClients
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.io.InputStream

fun main() {
    val cloud = HuaweiCloudService()

//    cloud.createBucket(BucketParams("lizheng-test2", CreateBucketConfiguration("cn-east-3")))

    cloud.putObject(PutParams("lizheng-test2","abc.jpg",""))
}

@Service
class HuaweiCloudService:ObsApi {
//    private var createBucketTemplate = "<CreateBucketConfiguration " +
//            "xmlns=\"http://obs.cn-east-3.myhuaweicloud.com/doc/2022-02-23/\">\n" +
//            "<Location>" + REGION + "</Location>\n" +
//            "</CreateBucketConfiguration>"


    override fun createBucket(bucketParams: BucketParams) {
//        val httpClient:CloseableHttpClient = HttpClients.createDefault()
//        val requestTime = HuaweiCloudTools.getCloudUploadFormatDate()
//        val contentType = "application/xml"
//        val httpPut = HttpPut("http://${bucketParams.bucketName}.obs.$REGION.myhuaweicloud.com")
//        httpPut.addHeader("Date",requestTime)
//        httpPut.addHeader("Content-Type",contentType)
//
//        val contentMD5:String = ""
//        val canonicalizeHeaders = ""
//        val canonicalizeResource = "/${bucketParams.bucketName}/"
//        val canonicalString = "PUT\n" +
//                "$contentMD5\n" +
//                "$contentType\n" +
//                "$requestTime\n" +
//                "$canonicalizeHeaders$canonicalizeResource"
//
//        println("canonicalString : $canonicalString")
//
//        val signature = HuaweiCloudTools.signWithHmacSha1(SECURITY_KEY,canonicalString)
//        httpPut.addHeader("Authorization","OBS $ACCESS_KEY:$signature")
//        httpPut.entity = StringEntity(createBucketTemplate)
//
//        val httpResponse:CloseableHttpResponse = httpClient.execute(httpPut)
//
//        println("Request Message:")
//        println("${httpPut.requestLine}")
//
//        for(header in httpPut.allHeaders){
//            println("${header.name} : ${header.value}")
//        }
//
//        println("${httpPut.entity.content}")
//
//        println("Response Message:")
//        println("${httpResponse.statusLine}")
//        for(header in httpResponse.allHeaders){
//            println("${header.name} : ${header.value}")
//        }
//
//        val reader = BufferedReader(InputStreamReader(httpResponse.entity.content))
//        val response = StringBuilder()
//        while (true){
//            val line = reader.readLine() ?: break
//            response.append(line)
//        }
//        reader.close()
//
//        println(response)
//
//        httpClient.close()
    }





    override fun putBucketLifecycleConfiguration(lifecycleParams: LifecycleParams) {
        TODO("Not yet implemented")
    }

    override fun deleteBucket(bucketName: String) {
        TODO("Not yet implemented")
    }

    override fun putObject(putParams: PutParams) {

        val httpClient = HttpClients.createDefault()
        val requestTime = HuaweiCloudTools.getCloudUploadFormatDate()
        val httpPut = HttpPut("http://${putParams.bucketName}.obs.$REGION.myhuaweicloud.com/${putParams.key}")
        httpPut.addHeader("Date",requestTime)

        val contentMD5 = ""
        val contentType = ""
        val canonicalizeHeaders = ""
        val canonicalizeResource = "/${putParams.bucketName}/${putParams.key}"
        val strToSign = "PUT\n" +
                "$contentMD5\n" +
                "$contentType\n" +
                "$requestTime\n" +
                "$canonicalizeHeaders$canonicalizeResource"

        val signature = HuaweiCloudTools.signWithHmacSha1(SECURITY_KEY,strToSign)


        val entity = if(putParams.body is String){
            InputStreamEntity(FileInputStream(putParams.body as String))
        }else if(putParams.body is InputStream){
            InputStreamEntity(putParams.body as InputStream)
        } else{
            throw UploadException(ManagementExceptionCode.BODY_TYPE_ERROR,"body error")
        }

        httpPut.entity = entity

        httpPut.addHeader("Authorization","OBS $ACCESS_KEY:$signature")
        val httpResponse = httpClient.execute(httpPut)

        println("Request Message:")
        println("${httpPut.requestLine}")

        for(header in httpPut.allHeaders){
            println("${header.name} : ${header.value}")
        }

        println("${httpPut.entity.content}")

        println("Response Message:")
        println("${httpResponse.statusLine}")

        if(httpResponse.statusLine.statusCode != 200){
            throw UploadException(ManagementExceptionCode.FILE_UPLOAD_FAILED,"file upload error")
        }

        for(header in httpResponse.allHeaders){
            println("${header.name} : ${header.value}")
        }
        httpClient.close()
    }

    override fun getObject(getParams: GetParams): Any? {
        TODO("Not yet implemented")
    }

    override fun deleteObject(deleteParams: DeleteParams) {
        TODO("Not yet implemented")
    }
}