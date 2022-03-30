package cn.sunline.saas.huaweicloud.config

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
class HuaweiCloudConfig {

    @Value("\${huawei.cloud.accessKey}")
    lateinit var accessKey:String
    @Value("\${huawei.cloud.securityKey}")
    lateinit var securityKey:String
    @Value("\${huawei.cloud.region}")
    lateinit var region:String
    @Value("\${huawei.cloud.bucketName}")
    lateinit var bucketName:String


    fun getCloudUploadFormatDate(): String {
       return DateTime.now().withZone(DateTimeZone.forID("UTC")).toString("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
    }

    fun signWithHmacSha1(sk:String,canonicalString:String):String{
        val signingKey = SecretKeySpec(sk.toByteArray(charset("UTF-8")),"HmacSHA1")
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(signingKey)
        return Base64.getEncoder().encodeToString(mac.doFinal(canonicalString.toByteArray(charset("UTF-8"))))
    }
}