package cn.sunline.saas.test.steps.config

import cn.sunline.saas.test.steps.dto.DTOPdpaItem
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.cucumber.java.DocStringType
import io.cucumber.java.en.Given
import io.cucumber.spring.CucumberContextConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@CucumberContextConfiguration
class CucumberConfig {
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
                    "    \"username\":\"test\",\n" +
                    "    \"password\":\"test\"\n" +
                    "}"

            val response = restAssuredConfig.post(restAssuredConfig.setManagementUrl("auth/login"), body)

            val username = response.jsonPath().getString("data.username")
            val token = response.jsonPath().getString("data.token")
            restAssuredConfig.setHeader("X-Authorization-Username",username)
            restAssuredConfig.setHeader("X-Authorization",token)
            restAssuredConfig.setCookies(response.cookies)
        }
    }
}