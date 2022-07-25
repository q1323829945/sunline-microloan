package cn.sunline.saas.test.steps.pdpa

import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.test.steps.config.RestAssuredConfig
import cn.sunline.saas.test.steps.dto.DTOCustomerPdpaInformationChange
import cn.sunline.saas.test.steps.dto.DTOPdpaAdd
import cn.sunline.saas.test.steps.dto.DTOPdpaChange
import cn.sunline.saas.test.steps.dto.DTOPdpaItem
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import java.util.LinkedList

class PdpaInformationAccess {

    @Autowired
    private lateinit var restAssuredConfig: RestAssuredConfig

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private lateinit var pdpaId:String
    private lateinit var customerId:String
    private lateinit var pdpaInformationView:List<DTOPdpaItem>

    private val country = CountryType.CHN
    private val language = LanguageType.ENGLISH



    @Given("init pdpa")
    fun `init pdpa`() {
        val checkExist = restAssuredConfig.get(restAssuredConfig.setManagementUrl("pdpa/${country}/${language}/retrieve"))
        if(checkExist.statusCode != 200){
            val response = restAssuredConfig.post(
                restAssuredConfig.setManagementUrl("pdpa"),
                DTOPdpaAdd(
                    country,
                    language,
                    items()
                ),)
            val id = response.jsonPath().get<String>("data.id")
            this.pdpaId = id
        } else {
            val id = checkExist.jsonPath().get<String>("data.id")
            this.pdpaId = id

            val getItems = checkExist.jsonPath().get<ArrayList<*>>("data.pdpaInformation")

            if(items() != objectMapper.convertValue<List<DTOPdpaItem>>(getItems)){
                restAssuredConfig.put(restAssuredConfig.setManagementUrl("pdpa/$id"), DTOPdpaChange(items()))
            }
        }
    }

    @Given("there are customer {string} already confirm PDPA authorization")
    fun `there are customer {string} already confirm PDPA authorization`(customerId:String){
        this.customerId = customerId
        val response = restAssuredConfig.get(restAssuredConfig.setMicroLoanUrl("pdpa/customer/$customerId"))
        val data = response.jsonPath().get<LinkedHashMap<String,*>?>("data")
        //TODO:正常流程是申请的时候把权限加上去。
        if(data == null){
            restAssuredConfig.post(
                restAssuredConfig.setManagementUrl("customer/pdpa/confirm"),
                DTOCustomerPdpaInformationChange(
                    customerId,
                    pdpaId,
                    "11111"
                )
            )
        }

    }

    @When("access PDPA information")
    fun `access PDPA information`(){
        val response = restAssuredConfig.get(restAssuredConfig.setMicroLoanUrl("pdpa/customer/$customerId"))
        val pdpaInformation = response.jsonPath().get<ArrayList<*>>("data.pdpaInformation")
        pdpaInformationView = objectMapper.convertValue(pdpaInformation)
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
                "                        \"label\": \"name\",\n" +
                "                        \"name\": \"name\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"alias\",\n" +
                "                        \"name\": \"alias\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"name pinyin\",\n" +
                "                        \"name\": \"name pinyin\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"alias pinyin\",\n" +
                "                        \"name\": \"alias pinyin\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"gender\",\n" +
                "                        \"name\": \"gender\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"birth\",\n" +
                "                        \"name\": \"birth\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"internationgal\",\n" +
                "                        \"name\": \"internationgal\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"register address\",\n" +
                "                        \"name\": \"register address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"hdb type\",\n" +
                "                        \"name\": \"hdb type\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"address\",\n" +
                "                        \"name\": \"address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"notice\",\n" +
                "                        \"name\": \"notice\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"mobile phone\",\n" +
                "                        \"name\": \"mobile phone\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"email\",\n" +
                "                        \"name\": \"email\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"item\": \"perpon informationn\",\n" +
                "                \"information\":\n" +
                "                [\n" +
                "                    {\n" +
                "                        \"label\": \"outline\",\n" +
                "                        \"name\": \"outline\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"compangy\",\n" +
                "                        \"name\": \"compangy\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"address\",\n" +
                "                        \"name\": \"address\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"uens\",\n" +
                "                        \"name\": \"uens\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"finance\",\n" +
                "                        \"name\": \"finance\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"capital\",\n" +
                "                        \"name\": \"capital\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"leader\",\n" +
                "                        \"name\": \"leader\"\n" +
                "                    },\n" +
                "                    {\n" +
                "                        \"label\": \"shareholder\",\n" +
                "                        \"name\": \"shareholder\"\n" +
                "                    }\n" +
                "                ]\n" +
                "            }\n" +
                "        ]"
        return objectMapper.readValue(json)
    }
}