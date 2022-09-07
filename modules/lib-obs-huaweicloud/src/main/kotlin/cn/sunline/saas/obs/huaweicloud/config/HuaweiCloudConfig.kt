package cn.sunline.saas.obs.huaweicloud.config

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Component
@ConfigurationProperties(prefix = "huawei.cloud.obs")
class HuaweiCloudConfig(
    var accessKey:String = "",
    var securityKey:String = "",
    var region:String = "",
    var bucketName:String = "",
    var style:String = ""
) {


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