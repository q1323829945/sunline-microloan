package cn.sunline.saas.test.steps.test

import cn.sunline.saas.test.steps.config.RestAssuredConfig
import io.cucumber.java.en.Given
import org.springframework.beans.factory.annotation.Autowired

class TestFeature {
    @Autowired
    private lateinit var restAssuredConfig: RestAssuredConfig

    @Given("test {string}")
    fun test(string:String){
        println(string)
        val body = "{\n" +
                "    \"username\":\"admin\",\n" +
                "    \"password\":\"admin\"\n" +
                "}"

        val response = restAssuredConfig.post(restAssuredConfig.setManagementUrl("auth/login"), body)
        response.print()
    }
}