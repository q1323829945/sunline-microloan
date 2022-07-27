package cn.sunline.saas.test.steps.pdpa

import cn.sunline.saas.test.steps.config.RestAssuredConfig
import cn.sunline.saas.test.steps.dto.DTOCustomerPdpaInformationChange
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired

class PdpaWithdraw {
    @Autowired
    private lateinit var restAssuredConfig: RestAssuredConfig

    private lateinit var customerId:String
    private var authority:String? = null

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Given("There are customer {string} PDPA authorization and consent records")
    fun `There are customer {string} PDPA authorization and consent records`(customerId:String) {

        this.customerId = customerId
        val response = restAssuredConfig.get(restAssuredConfig.setMicroLoanUrl("/pdpa/customer/$customerId"))
        val data = response.jsonPath().get<LinkedHashMap<String,*>?>("data")
        //TODO:正常流程是申请的时候把权限加上去。
        if(data == null){
            val pdpaResponse = restAssuredConfig.get(restAssuredConfig.setManagementUrl("/pdpa/CHN/ENGLISH/retrieve"))

            restAssuredConfig.post(
                restAssuredConfig.setManagementUrl("/customer/pdpa/confirm"),
                DTOCustomerPdpaInformationChange(
                    customerId,
                    pdpaResponse.jsonPath().get<String>("data.id"),
                    "11111"
                )
            )
        }

    }

    @When("select withdraw")
    fun `select withdraw`(){
        restAssuredConfig.put(restAssuredConfig.setMicroLoanUrl("/pdpa/customer/withdraw/$customerId"),null)
        Thread.sleep(3000)
        val response = restAssuredConfig.get(restAssuredConfig.setMicroLoanUrl("/pdpa/customer/$customerId"))
        val authority = response.jsonPath().get<String?>("data")
        this.authority = authority
    }

    @Then("the customer PDPA authorization is {string}")
    fun `the customer PDPA authorization is {string}`(status:String?){
        val replace = replace(status)
        Assertions.assertEquals(authority,replace)
    }

    private fun replace(status: String?):String?{
        return if(status.isNullOrEmpty()){
            null
        } else {
            status
        }
    }
}