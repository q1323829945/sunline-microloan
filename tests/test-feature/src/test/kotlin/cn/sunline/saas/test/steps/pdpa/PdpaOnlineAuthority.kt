package cn.sunline.saas.test.steps.pdpa

import cn.sunline.saas.test.steps.config.RestAssuredConfig
import cn.sunline.saas.test.steps.dto.DTOPdpaAuthority
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired

const val ELECTRONIC_SIGNATURE = "electronicSignature"
const val FACE_RECOGNITION = "faceRecognition"
const val FINGERPRINT = "fingerprint"

class PdpaOnlineAuthority {
    @Autowired
    private lateinit var restAssuredConfig: RestAssuredConfig

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    private var isElectronicSignature = false
    private var isFaceRecognition = false
    private var isFingerprint = false
    private lateinit var pdpaAuthority:DTOPdpaAuthority

    private var answerAuthorityWayElectronicSignature:String? = null
    private var answerAuthorityWayFaceRecognition:String? = null
    private var answerAuthorityWayFingerprint:String? = null

    @Given("the authority way is {string}")
    fun `the authority way is {string}`(authorityWay:String) {
        val authorityWays = authorityWay.split(",")
        authorityWayConvert(authorityWays)

        restAssuredConfig.put(
            restAssuredConfig.setManagementUrl("/pdpa/authority"),
            DTOPdpaAuthority(
                isElectronicSignature = isElectronicSignature,
                isFaceRecognition = isFaceRecognition,
                isFingerprint = isFingerprint
            )
        )
    }

    @When("query the authority way")
    fun `query the authority way`() {
        val response = restAssuredConfig.get(restAssuredConfig.setManagementUrl("/pdpa/authority"))

        val data = response.jsonPath().get<LinkedHashMap<String,*>>("data")

        this.pdpaAuthority = objectMapper.convertValue(data)
    }

    @Then("the customer PDPA authority way is {string}")
    fun `the customer PDPA authority way is {string}`(answerAuthorityWay:String) {
        val answerAuthorityWays = answerAuthorityWay.split(",")
        answerAuthorityWayConvert(answerAuthorityWays)
        val authorityWayElectronicSignature = if(pdpaAuthority.isElectronicSignature) ELECTRONIC_SIGNATURE else null
        val authorityWayFaceRecognition = if(pdpaAuthority.isFaceRecognition) FACE_RECOGNITION else null
        val authorityWayFingerprint = if(pdpaAuthority.isFingerprint) FINGERPRINT else null
        Assertions.assertEquals(authorityWayElectronicSignature,answerAuthorityWayElectronicSignature)
        Assertions.assertEquals(authorityWayFaceRecognition,answerAuthorityWayFaceRecognition)
        Assertions.assertEquals(authorityWayFingerprint,answerAuthorityWayFingerprint)


    }

    private fun authorityWayConvert(AuthorityWays:List<String>){
        AuthorityWays.forEach {
            if(ELECTRONIC_SIGNATURE == it){
                this.isElectronicSignature = true
            }
            if(FACE_RECOGNITION == it){
                this.isFaceRecognition = true
            }
            if(FINGERPRINT == it){
                this.isFingerprint = true
            }
        }
    }

    private fun answerAuthorityWayConvert(answerAuthorityWays:List<String>){
        answerAuthorityWays.forEach {
            if(ELECTRONIC_SIGNATURE == it){
                this.answerAuthorityWayElectronicSignature = it
            }
            if(FACE_RECOGNITION == it){
                this.answerAuthorityWayFaceRecognition = it
            }
            if(FINGERPRINT == it){
                this.answerAuthorityWayFingerprint = it
            }
        }
    }

}