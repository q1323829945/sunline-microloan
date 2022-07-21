package cn.sunline.saas.cucumber.pdpa

import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.pdpa.modules.dto.DTOCustomerPdpaInformation
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAdd
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaItem
import cn.sunline.saas.pdpa.services.CustomerPdpaInformationService
import cn.sunline.saas.pdpa.services.PdpaService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired

class PdpaInformationAccess {

    @Autowired
    private lateinit var customerPdpaInformationService: CustomerPdpaInformationService
    @Autowired
    private lateinit var pdpaService: PdpaService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private lateinit var pdpaId:String
    private lateinit var customerId:String
    private lateinit var pdpaInformationView:List<DTOPdpaItem>

    @Given("init pdpa")
    fun `init pdpa`() {
        val pdpa = pdpaService.addOne(
            DTOPdpaAdd(
                country = CountryType.CHN,
                language = LanguageType.CHINESE,
                pdpaInformation = items()
            )
        )

        this.pdpaId = pdpa.id
    }

    @Given("there are customer {string} already confirm PDPA authorization")
    fun `there are customer {string} already confirm PDPA authorization`(customerId:String){
        customerPdpaInformationService.getAndRegisterCustomerPdpaInformation(
            DTOCustomerPdpaInformation(
                customerId = customerId,
                pdpaId = pdpaId,
                electronicSignature = "6666",
                faceRecognition = "6666",
                fingerprint = "6666",
            )
        )

        this.customerId = customerId
    }

    @When("access PDPA information")
    fun `access PDPA information`(){
        val customerInformation = customerPdpaInformationService.getAndRegisterCustomerPdpaInformation(DTOCustomerPdpaInformation(customerId))
        customerInformation.pdpaId?.run {
            val pdpa = pdpaService.getDTOPdpaView(this)

            pdpaInformationView = pdpa.pdpaInformation!!
        }
    }

    @Then("obtain PDPA authorization information")
    fun `obtain PDPA authorization information`(items:List<DTOPdpaItem>){
        Assertions.assertEquals(pdpaInformationView, items)

    }


    private fun items():List<DTOPdpaItem>{
        val json = "        [\n" +
                "            {\n" +
                "                \"item\": \"business information\",\n" +
                "                \"information\":\n" +
                "                [\n" +
                "                    {\n" +
                "                        \"key\": \"name\",\n" +
                "                        \"name\": \"name\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"alias\",\n" +
                "                        \"name\": \"alias\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"name pinyin\",\n" +
                "                        \"name\": \"name pinyin\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"alias pinyin\",\n" +
                "                        \"name\": \"alias pinyin\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"gender\",\n" +
                "                        \"name\": \"gender\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"birth\",\n" +
                "                        \"name\": \"birth\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"internationgal\",\n" +
                "                        \"name\": \"internationgal\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"register address\",\n" +
                "                        \"name\": \"register address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"hdb type\",\n" +
                "                        \"name\": \"hdb type\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"address\",\n" +
                "                        \"name\": \"address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"notice\",\n" +
                "                        \"name\": \"notice\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"mobile phone\",\n" +
                "                        \"name\": \"mobile phone\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"email\",\n" +
                "                        \"name\": \"email\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"item\": \"perpon informationn\",\n" +
                "                \"information\":\n" +
                "                [\n" +
                "                    {\n" +
                "                        \"key\": \"outline\",\n" +
                "                        \"name\": \"outline\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"compangy\",\n" +
                "                        \"name\": \"compangy\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"address\",\n" +
                "                        \"name\": \"address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"uens\",\n" +
                "                        \"name\": \"uens\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"finance\",\n" +
                "                        \"name\": \"finance\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"capital\",\n" +
                "                        \"name\": \"capital\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"leader\",\n" +
                "                        \"name\": \"leader\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"key\": \"shareholder\",\n" +
                "                        \"name\": \"shareholder\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]"
        return objectMapper.readValue(json)
    }
}