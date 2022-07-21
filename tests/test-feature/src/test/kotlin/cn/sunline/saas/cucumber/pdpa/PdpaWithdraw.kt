package cn.sunline.saas.cucumber.pdpa

import cn.sunline.saas.pdpa.modules.db.CustomerPdpaInformation
import cn.sunline.saas.pdpa.modules.dto.DTOCustomerPdpaInformation
import cn.sunline.saas.pdpa.services.CustomerPdpaInformationService
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured.given
import io.restassured.config.RestAssuredConfig
import io.restassured.config.SSLConfig
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired

class PdpaWithdraw {
    @Autowired
    private lateinit var customerPdpaInformationService: CustomerPdpaInformationService

    private lateinit var customerId:String
    private lateinit var information:CustomerPdpaInformation

    @Given("There are customer {string} PDPA authorization and consent records")
    fun `There are customer {string} PDPA authorization and consent records`(customerId:String) {
        customerPdpaInformationService.getAndRegisterCustomerPdpaInformation(
            DTOCustomerPdpaInformation(
                customerId = customerId,
                pdpaId = "1111",
                electronicSignature = "path"
            )
        )

        this.customerId = customerId

        val a = given()
            .config(RestAssuredConfig.config().sslConfig(SSLConfig().relaxedHTTPSValidation()))
            .get("https://www.baidu.com")
            .then()
            .statusCode(200)
        println(a.toString())
    }

    @When("select withdraw")
    fun `select withdraw`(){
        val information = customerPdpaInformationService.withdraw(customerId.toLong())
        this.information = information
    }

    @Then("the customer PDPA authorization is {string}")
    fun `the customer PDPA authorization is {string}`(status:String?){
        val replace = replace(status)
        Assertions.assertEquals(information.pdpaId,replace)
        Assertions.assertEquals(information.electronicSignature,replace)
        Assertions.assertEquals(information.fingerprint,replace)
        Assertions.assertEquals(information.faceRecognition,replace)
    }

    private fun replace(status: String?):String?{
        return if(status.isNullOrEmpty()){
            null
        } else {
            status
        }
    }
}