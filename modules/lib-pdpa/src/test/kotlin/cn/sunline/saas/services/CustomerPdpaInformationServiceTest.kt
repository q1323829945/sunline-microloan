package cn.sunline.saas.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.pdpa.modules.dto.DTOCustomerPdpaInformation
import cn.sunline.saas.pdpa.modules.dto.DTOCustomerPdpaInformationChange
import cn.sunline.saas.pdpa.services.CustomerPdpaInformationService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerPdpaInformationServiceTest {
    @Autowired
    private lateinit var customerPdpaInformationService: CustomerPdpaInformationService


    @BeforeAll
    fun init() {
        ContextUtil.setTenant("12344566")
        customerPdpaInformationService.getAndRegisterCustomerPdpaInformation(DTOCustomerPdpaInformation("10086"))
    }

    @Test
    fun `get customer information`(){
        val information = customerPdpaInformationService.getAndRegisterCustomerPdpaInformation(
            DTOCustomerPdpaInformation("10086")
        )
        Assertions.assertThat(information).isNotNull
    }

    @Test
    fun confirm(){
        val information = customerPdpaInformationService.confirm(10086,
            DTOCustomerPdpaInformationChange(
                pdpaId = "10086",
                electronicSignature = "8888888"
            )
        )

        Assertions.assertThat(information.electronicSignature).isNotNull
        Assertions.assertThat(information.electronicSignature).isEqualTo("8888888")
    }

    @Test
    fun withdraw(){
        val information = customerPdpaInformationService.withdraw(10086)

        Assertions.assertThat(information.pdpaId).isNull()
        Assertions.assertThat(information.electronicSignature).isNull()
    }
}