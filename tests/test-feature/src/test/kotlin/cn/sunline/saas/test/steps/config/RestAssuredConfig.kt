package cn.sunline.saas.test.steps.config

import io.restassured.config.SSLConfig
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "rest-assured")
class RestAssuredConfig(
    var managementPath:String = "http://localhost:8083",
    var microLoanPath:String = "http://localhost:8084",
) {
    private val headers = mutableMapOf(
        "X-Authorization-Tenant" to "0",
    )

    private val query = mutableMapOf<String,String>()

    private val cookies = mutableMapOf<String,String>()

    fun setManagementUrl(path:String):String{
        return "$managementPath/$path"
    }

    fun setMicroLoanUrl(path:String):String{
        return "$microLoanPath/$path"
    }

    private fun given(): RequestSpecification {
        return io.restassured.RestAssured.given().config(io.restassured.config.RestAssuredConfig.config().sslConfig(SSLConfig().relaxedHTTPSValidation()))
    }

    fun setHeader(key:String,value:String){
        headers[key] = value
    }

    fun setHeaders(map:MutableMap<String,String>){
        headers.putAll(map)
    }

    fun getHeader(key:String):String?{
        return headers[key]
    }

    fun setCookie(key:String,value:String){
        cookies[key] = value
    }

    fun setCookies(map:MutableMap<String,String>){
        cookies.putAll(map)
    }

    fun setQuery(map:MutableMap<String,String>){
        headers.putAll(map)
    }

    fun get(url:String): Response {
        return given()
            .contentType(ContentType.JSON)
            .cookies(cookies)
            .headers(headers)
            .queryParams(query)
            .get(url)
    }

    fun post(url:String,body:Any):Response {
        return given()
            .contentType(ContentType.JSON)
            .cookies(cookies)
            .headers(headers)
            .body(body)
            .post(url)
    }

    fun put(url:String,body:Any?):Response {
        return if(body == null){
            given()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .headers(headers)
                .put(url)
        } else {
            given()
                .contentType(ContentType.JSON)
                .cookies(cookies)
                .headers(headers)
                .body(body)
                .put(url)
        }


    }

    fun delete(url:String,body:Any):Response {
        return given()
            .contentType(ContentType.JSON)
            .cookies(cookies)
            .headers(headers)
            .body(body)
            .post(url)
    }
}