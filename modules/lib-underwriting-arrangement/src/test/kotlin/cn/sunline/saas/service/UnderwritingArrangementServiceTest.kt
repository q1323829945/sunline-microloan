package cn.sunline.saas.service

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.underwriting.arrangement.model.dto.DTOUnderwritingArrangement
import cn.sunline.saas.underwriting.arrangement.model.dto.DTOUnderwritingArrangementAdd
import cn.sunline.saas.underwriting.arrangement.model.dto.DTOUnderwritingArrangementInvolvement
import cn.sunline.saas.underwriting.arrangement.service.UnderwritingArrangementService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UnderwritingArrangementServiceTest {
    @Autowired
    private lateinit var underwritingArrangementService: UnderwritingArrangementService

    @Autowired
    private lateinit var tenantService: TenantService

    @BeforeAll
    fun init(){
        ContextUtil.setTenant("123")

        tenantService.save(
            Tenant(
                id = 123,
                country = CountryType.CHN
            )
        )
    }

    @Test
    fun registered(){
        val arrangement = underwritingArrangementService.registered(
            dtoUnderwritingArrangementAdd = DTOUnderwritingArrangementAdd(
                agreementId = 1,
                underwriting = mutableListOf(
                    DTOUnderwritingArrangement(
                        startDate = "20220611",
                        endDate = "20220630",
                        involvements = mutableListOf(
                            DTOUnderwritingArrangementInvolvement(
                                party = 1,
                                primary = true,
                            )
                        )
                    )
                )
            )
        )


        Assertions.assertThat(arrangement).isNotNull
    }
}