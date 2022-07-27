package cn.sunline.saas.test.steps.config

import cn.sunline.saas.test.CucumberRunner
import cn.sunline.saas.test.steps.dto.DTOPdpaItem
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.cucumber.java.DocStringType
import io.cucumber.java.en.Given
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [CucumberRunner::class])
@CucumberContextConfiguration
@ConfigurationProperties(prefix = "cucumber-config")
class CucumberConfig(
    var loginPath:String = "http://localhost:8083",
    var username:String = "admin",
    var password:String = "admin",
) {
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var restAssuredConfig: RestAssuredConfig

    @DocStringType(contentType = "json")
    fun convert(json: String): List<DTOPdpaItem> {
        return objectMapper.readValue(json)
    }

    @Given("init tenant")
    fun initTenant(){
        if(restAssuredConfig.getHeader("X-Authorization") == null){
            val body = "{\n" +
                    "    \"username\":\"$username\",\n" +
                    "    \"password\":\"$password\"\n" +
                    "}"

            val response = restAssuredConfig.post("$loginPath/auth/login", body)

            val username = response.jsonPath().getString("data.username")
            val token = response.jsonPath().getString("data.token")
            restAssuredConfig.setHeader("X-Authorization-Username",username)
            restAssuredConfig.setHeader("X-Authorization",token)
            restAssuredConfig.setCookies(response.cookies)
        }
    }
}