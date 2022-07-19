package cn.sunline.saas.cucumber.pdpa

import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAdd
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaItem
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaView
import cn.sunline.saas.pdpa.services.PdpaService
import io.cucumber.java.en.And
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired

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
