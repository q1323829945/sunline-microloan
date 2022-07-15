package cn.sunline.saas.pdpa

import cn.sunline.saas.CucumberApplication
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.pdpa.modules.db.PdpaAuthority
import cn.sunline.saas.pdpa.modules.dto.DTOCustomerPdpaInformation
import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAuthority
import cn.sunline.saas.pdpa.services.PdpaAuthorityService
import io.cucumber.java.Before
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.cucumber.junit.Cucumber
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

const val ELECTRONIC_SIGNATURE = "electronicSignature"
const val FACE_RECOGNITION = "faceRecognition"
const val FINGERPRINT = "fingerprint"

@RunWith(Cucumber::class)
@SpringBootTest(classes = [CucumberApplication::class])
class PdpaOnlineAuthority {
    @Autowired
    private lateinit var pdpaAuthorityService: PdpaAuthorityService


    private var isElectronicSignature = false
    private var isFaceRecognition = false
    private var isFingerprint = false
    private lateinit var pdpaAuthority:PdpaAuthority

    private var answerAuthorityWayElectronicSignature:String? = null
    private var answerAuthorityWayFaceRecognition:String? = null
    private var answerAuthorityWayFingerprint:String? = null

    @Given("init pdpa authority way")
    fun `init pdpa authority way `() {
        pdpaAuthorityService.register()
    }

    @Given("the authority way is {string}")
    fun `the authority way is {string}`(authorityWay:String) {
        val authorityWays = authorityWay.split(",")
        authorityWayConvert(authorityWays)
        pdpaAuthorityService.updateOne(
            DTOPdpaAuthority(
                isElectronicSignature = isElectronicSignature,
                isFaceRecognition = isFaceRecognition,
                isFingerprint = isFingerprint
            )
        )
    }

    @When("query the authority way")
    fun `query the authority way`() {
        val pdpaAuthority = pdpaAuthorityService.getOne()
        this.pdpaAuthority = pdpaAuthority
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