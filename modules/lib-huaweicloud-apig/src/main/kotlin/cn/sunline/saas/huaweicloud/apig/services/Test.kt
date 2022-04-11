package cn.sunline.saas.huaweicloud.apig.services

import cn.sunline.saas.global.constant.HttpRequestMethod
import cn.sunline.saas.HttpConfig
import com.google.gson.Gson
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.springframework.http.MediaType
import java.net.URLEncoder
import java.security.MessageDigest
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec



fun sign(map:Map<String,String>,bodyStr:String):String{
    var canonicalRequest  = "GET\n" +
            "/v1.0/apigw/envs/\n" +
            "name=${URLEncoder.encode(environmentName, "utf-8")}\n"
    map.forEach { (k, v) ->
        canonicalRequest += "${k.lowercase()}:${v.trim()}\n"
    }
    map.forEach { (k, v) ->
        canonicalRequest += "${k.lowercase()};"
    }
    canonicalRequest = canonicalRequest.substring(0,canonicalRequest.length-1)
    canonicalRequest += "\n" + sha256(bodyStr)

    println(canonicalRequest)
    println()
    println()
    return sha256(canonicalRequest)
}

fun main() {
    //uri
    val uri = getUri("/v1.0/apigw/envs?name=${URLEncoder.encode(environmentName, "utf-8")}")
    //get httpMethod
    val httpMethod = httpConfig.getHttpMethod(HttpRequestMethod.GET, uri, getHeaderMap())

    //sendClint
    httpConfig.sendClient(httpMethod)

    //get responseBody
    val responseBody = httpConfig.getResponseBody(httpMethod)

    val map = Gson().fromJson(responseBody, Map::class.java)

    println(map)


}

var httpConfig: HttpConfig = HttpConfig()
val environmentName = "dev"

fun getUri(path:String):String{
    return "https://apig.cn-east-3.myhuaweicloud.com$path"
}

fun getHeaderMap():MutableMap<String,String>{

    val headerMap = mutableMapOf<String, String>()
    headerMap["Content-Type"] = MediaType.APPLICATION_JSON_VALUE
    headerMap["Host"] = "apig.cn-east-3.myhuaweicloud.com"
    headerMap["X-Sdk-Date"] = shijian
    headerMap["X-Domain-Id"] = "7fee3d7348a74d0ebb041e328cc41ccd"
    headerMap["X-Project-Id"] = "a7bbc97fa36648d4a9f4fea6d1534ab8"
    headerMap["Project-Id"] = "a7bbc97fa36648d4a9f4fea6d1534ab8"

    var authorization = "SDK-HMAC-SHA256" +
            " Access=KZGUMLJ5Z9MTEGBSVVQD," +
            " SignedHeaders="
    headerMap.forEach { (k, v) ->
        authorization += "${k.lowercase()};"
    }
    authorization = authorization.substring(0,authorization.length-1)
    authorization += "," +
            " Signature=${sign2(headerMap,"")}"

    headerMap["Authorization"] = authorization

    println(authorization)

    return headerMap
}

fun signWithHmacSha1(sk:String,canonicalString:String):String{
    val signingKey = SecretKeySpec(sk.toByteArray(charset("UTF-8")),"HmacSHA1")
    val mac = Mac.getInstance("HmacSHA1")
    mac.init(signingKey)

    return hexto16(mac.doFinal(canonicalString.toByteArray(charset("UTF-8"))))
}
val shijian = "20220408T072651Z"

fun sign2(map:Map<String,String>,bodyStr:String):String{
    val stringToSign = "SDK-HMAC-SHA256\n" +
            "${shijian}\n" +
            sign(map,bodyStr)

    return  signWithHmacSha1("01nNSdo9au0koJ39DlEFhmYnFF5EUpExG8eLJ68O", stringToSign)
}


fun getTime():String{
    return DateTime.now().withZone(DateTimeZone.forID("UTC")).toString("YYYYMMDD'T'HHMMSS'Z'", Locale.ENGLISH)
}

fun sha256(str:String):String{
    val m = MessageDigest.getInstance("SHA-256")
    m.update(str.toByteArray(charset("UTF-8")))

    return hexto16(m.digest())
}

fun hexto16(bytes:ByteArray):String{
    val sb = StringBuffer()
    bytes.forEach {
        val temp = Integer.toHexString(it.toInt() and 0xFF)
        if(temp.length == 1){
            sb.append("0")
        }
        sb.append(temp)
    }
    return sb.toString()
}