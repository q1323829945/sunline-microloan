package cn.sunline.saas.test.steps.pdpa

import cn.sunline.saas.test.steps.config.RestAssuredConfig
import cn.sunline.saas.test.steps.dto.DTOPdpaAdd
import cn.sunline.saas.test.steps.dto.DTOPdpaChange
import cn.sunline.saas.test.steps.dto.DTOPdpaItem
import cn.sunline.saas.test.steps.dto.DTOPdpaView
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired

class PdpaFeature {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var restAssuredConfig: RestAssuredConfig

    private lateinit var country: String
    private lateinit var language: String
    private lateinit var items: List<DTOPdpaItem>
    private lateinit var id: String
    private lateinit var pdpaView: DTOPdpaView


    @Given("country is {string}")
    fun `country is {string}`(country: String) {
        this.country = country
    }

    @And("language is {string}")
    fun `language is {string}`(language: String) {
        this.language = language
    }

    @And("the configured information item")
    fun `the configured information item`(items: List<DTOPdpaItem>) {
        this.items = items
    }

    @When("add PDPA information")
    fun `add PDPA information`() {
        val checkExist = restAssuredConfig.get(restAssuredConfig.setManagementUrl("/pdpa/${country}/${language}/retrieve"))
        if(checkExist.statusCode != 200){
            val response = restAssuredConfig.post(
                restAssuredConfig.setManagementUrl("/pdpa"),
                DTOPdpaAdd(
                    country,
                    language,
                    items
                ),)
            val id = response.jsonPath().get<String>("data.id")
            this.id = id
        } else {
            val id = checkExist.jsonPath().get<String>("data.id")
            this.id = id


            val getItems = checkExist.jsonPath().get<ArrayList<*>>("data.pdpaInformation")

            if(items != objectMapper.convertValue<List<DTOPdpaItem>>(getItems)){
                restAssuredConfig.put(restAssuredConfig.setManagementUrl("/pdpa/$id"), DTOPdpaChange(items))
            }
        }

    }

    @And("get PDPA information")
    fun `get PDPA information`() {
        val response = restAssuredConfig.get(restAssuredConfig.setManagementUrl("/pdpa/$id"))

        val data = response.jsonPath().get<LinkedHashMap<String,*>>("data")
        pdpaView = objectMapper.convertValue(data)
    }

    @Then("the {string} PDPA language is {string} configure is items")
    fun `the {string} PDPA language is {string} configure is items`(country: String, language: String, items: List<DTOPdpaItem>) {
        Assertions.assertEquals(pdpaView.country, country)
        Assertions.assertEquals(pdpaView.language, language)
        Assertions.assertEquals(pdpaView.pdpaInformation, items)
    }


}
