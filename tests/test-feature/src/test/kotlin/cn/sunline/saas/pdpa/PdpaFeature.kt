package cn.sunline.saas.pdpa

import cn.sunline.saas.CucumberApplication
import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAdd
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaItem
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaView
import cn.sunline.saas.pdpa.services.PdpaService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.cucumber.java.Before
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.junit.Cucumber
import io.cucumber.spring.CucumberContextConfiguration
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@RunWith(Cucumber::class)
@SpringBootTest(classes = [CucumberApplication::class])
class PdpaFeature {

    @Autowired
    private lateinit var pdpaService: PdpaService

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
        val (id1) = pdpaService.addOne(
            DTOPdpaAdd(
                CountryType.valueOf(country),
                LanguageType.valueOf(language),
                items
            )
        )
        id = id1
    }

    @And("get PDPA information")
    fun `get PDPA information`() {
        pdpaView = pdpaService.getDTOPdpaView(id.toLong())
    }

    @Then("the {string} PDPA language is {string} configure is items")
    fun `the {string} PDPA language is {string} configure is items`(country: String, language: String, items: List<DTOPdpaItem>) {
        Assertions.assertEquals(pdpaView.country.name, country)
        Assertions.assertEquals(pdpaView.language.name, language)
        Assertions.assertEquals(pdpaView.pdpaInformation, items)
    }


}
