package cn.sunline.saas.service

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.partner.integrated.model.*
import cn.sunline.saas.partner.integrated.model.db.PartnerIntegrated
import cn.sunline.saas.partner.integrated.service.PartnerIntegratedService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PartnerIntegratedServiceTest {
    @Autowired
    private lateinit var partnerIntegratedService: PartnerIntegratedService


    @BeforeAll
    fun init(){
        ContextUtil.setTenant("123")

        val partnerIntegrated = partnerIntegratedService.registered(
            partnerIntegrated = PartnerIntegrated(
                tenantId = 123,
                customerCreditRatingPartner = CustomerCreditRatingPartner.TEST,
                creditRiskPartner = CreditRiskPartner.TEST,
                regulatoryCompliancePartner = RegulatoryCompliancePartner.TEST,
                fraudEvaluationPartner = FraudEvaluationPartner.TEST,
                disbursementPartner = DisbursementPartner.TEST,
                financialAccountingPartner = FinancialAccountingPartner.TEST,
            )
        )

        Assertions.assertThat(partnerIntegrated).isNotNull
    }

    @Test
    fun `get one`(){
        val partnerIntegrated = partnerIntegratedService.get()

        Assertions.assertThat(partnerIntegrated).isNotNull
    }
}