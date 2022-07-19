package cn.sunline.saas.rest_assured.pdpa

import cn.sunline.saas.rest_assured.config.RestAssuredConfig
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PdpaFeature {

    @Autowired
    private lateinit var restAssuredConfig: RestAssuredConfig

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    @BeforeAll
    fun login(){
        val body = "{\n" +
                "    \"username\":\"test\",\n" +
                "    \"password\":\"test\"\n" +
                "}"

        val response = restAssuredConfig.post("https://quickloan-management-demo.finline.app/auth/login", body)
        val username = response.jsonPath().getString("data.username")
        val token = response.jsonPath().getString("data.token")
        restAssuredConfig.setHeader("X-Authorization-Username",username)
        restAssuredConfig.setHeader("X-Authorization",token)
        restAssuredConfig.setCookies(response.cookies)
    }

    @Test
    @Disabled
    fun `add pdpa`(){
//        val body = restAssuredConfig.post("https://quickloan-management-demo.finline.app/management/pdpa",pdpaBody())

//        body.jsonPath().get<String>("data")
    }


    private fun pdpaBody():String{
        return """
        {
            "country":"SGP",
            "language":"ENGLISH",
            "pdpaInformation":[
                    {
                        "item": "on behalf of",
                        "information": 
                        [
                            {
                                "key": "company type",
                                "name": "company type"
                            },
                            {
                                "key": "Juristic name",
                                "name": "Juristic name"
                            },
                            {
                                "key": "Juristic ID",
                                "name": "Juristic ID"
                            },
                            {
                                "key": "Register Date",
                                "name": "Register Date"
                            }
                        ]
                    },
                    {
                        "item": "contact information",
                        "information": 
                        [
                            {
                                "key": "Mr./Mrs./Ms./Otiher",
                                "name": "Mr./Mrs./Ms./Otiher"
                            },
                            {
                                "key": "Contact perpon",
                                "name": "Contact perpon"
                            },
                            {
                                "key": "Address",
                                "name": "Address"
                            },
                            {
                                "key": "Road",
                                "name": "Road"
                            },
                            {
                                "key": "Sub district",
                                "name": "Sub district"
                            },
                            {
                                "key": "District",
                                "name": "District"
                            },
                            {
                                "key": "Province",
                                "name": "Province"
                            },
                            {
                                "key": "Postal code",
                                "name": "Postal code"
                            },
                            {
                                "key": "Telphone No.",
                                "name": "Telphone No."
                            },
                            {
                                "key": "fax No",
                                "name": "fax No"
                            }
                        ]
                    },
                    {
                        "item": "Business information",
                        "information": 
                        [
                            {
                                "key": "businesstype",
                                "name": "businesstype"
                            },
                            {
                                "key": "average revenue",
                                "name": "average revenue"
                            },
                            {
                                "key": "anerage revenue per year",
                                "name": "anerage revenue per year"
                            },
                            {
                                "key": "No.of Employees",
                                "name": "No.of Employees"
                            }
                        ]
                    },
                    {
                        "item": "Loan Details",
                        "information": 
                        [
                            {
                                "key": "what is your purpose",
                                "name": "what is your purpose"
                            },
                            {
                                "key": "requlred loan amount",
                                "name": "requlred loan amount"
                            },
                            {
                                "key": "loan type",
                                "name": "loan type"
                            },
                            {
                                "key": "other required products/services",
                                "name": "other required products/services"
                            },
                            {
                                "key": "negative financial record",
                                "name": "negative financial record"
                            },
                            {
                                "key": "convenient time to call",
                                "name": "convenient time to call"
                            }
                        ]
                    },
                    {
                        "item": "Preferred Branch",
                        "information": 
                        [
                            {
                                "key": "select province",
                                "name": "select province"
                            },
                            {
                                "key": "select branch",
                                "name": "select branch"
                            }
                        ]
                    }
                ]
        }
        """.trimIndent()
    }
}