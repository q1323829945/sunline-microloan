package cn.sunline.saas.channel.services

import cn.sunline.saas.global.model.CurrencyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setTimeZone
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.statistics.modules.TransactionType
import cn.sunline.saas.channel.statistics.modules.dto.DTOBusinessDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOBusinessDetailQueryParams
import cn.sunline.saas.channel.statistics.services.BusinessDetailService
import org.assertj.core.api.Assertions
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BusinessDetailServiceTest(@Autowired val businessDetailService: BusinessDetailService,@Autowired val tenantDateTime: TenantDateTime) {

    @BeforeAll
    fun `init`(){
        ContextUtil.setTimeZone(DateTimeZone.UTC)
        ContextUtil.setTenant("12344566")
        businessDetailService.saveBusinessDetail(
            DTOBusinessDetail(
                agreementId = 1,
                customerId = 1,
                amount = BigDecimal(1100),
                currency = CurrencyType.CNY,
                transactionType = TransactionType.PAYMENT,
            )
        )
    }

    @Test
    fun `get first by agreement`(){
        val business = businessDetailService.getFirstByAgreementId(1)
        Assertions.assertThat(business).isNotNull


        val business2 = businessDetailService.getFirstByAgreementId(2)
        Assertions.assertThat(business2).isNull()
    }

    @Test
    fun `get count`(){
        val startDate = tenantDateTime.now().plusDays(-1).toDate()
        val endDate = tenantDateTime.now().toDate()
        val count = businessDetailService.getGroupByBusinessCount(DTOBusinessDetailQueryParams(startDate,endDate))


        Assertions.assertThat(count[0].paymentAmount).isEqualTo(BigDecimal(1100).setScale(2))
        Assertions.assertThat(count[0].repaymentAmount).isEqualTo(BigDecimal(0))
    }
}