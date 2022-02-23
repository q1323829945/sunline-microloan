package cn.sunline.saas.huaweicloud.config

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HuaweiCloudTools {

    fun getCloudUploadFormatDate():String{
        val serverDateFormat: DateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
        serverDateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return serverDateFormat.format(System.currentTimeMillis())
    }

    fun signWithHmacSha1(sk:String,canonicalString:String):String{
        val signingKey = SecretKeySpec(sk.toByteArray(charset("UTF-8")),"HmacSHA1")
        val mac = Mac.getInstance("HmacSHA1")
        mac.init(signingKey)
        return Base64.getEncoder().encodeToString(mac.doFinal(canonicalString.toByteArray(charset("UTF-8"))))
    }

}