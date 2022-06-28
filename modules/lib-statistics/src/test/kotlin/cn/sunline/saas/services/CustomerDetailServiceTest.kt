package cn.sunline.saas.services

import cn.sunline.saas.global.constant.PartyType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setTimeZone
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.statistics.modules.dto.DTOCustomerDetail
import cn.sunline.saas.statistics.modules.dto.DTOCustomerDetailQueryParams
import cn.sunline.saas.statistics.services.CustomerDetailService
import org.assertj.core.api.Assertions
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerDetailServiceTest(@Autowired val customerDetailService: CustomerDetailService,@Autowired val tenantDateTime: TenantDateTime) {
    @BeforeAll
    fun `init`(){
        ContextUtil.setTimeZone(DateTimeZone.UTC)
        ContextUtil.setTenant("12344566")
        customerDetailService.saveCustomerDetail(DTOCustomerDetail(1,PartyType.PERSON))
        customerDetailService.saveCustomerDetail(DTOCustomerDetail(2,PartyType.PERSON))
        customerDetailService.saveCustomerDetail(DTOCustomerDetail(3,PartyType.ORGANISATION))
        customerDetailService.saveCustomerDetail(DTOCustomerDetail(4,PartyType.ORGANISATION))
        customerDetailService.saveCustomerDetail(DTOCustomerDetail(5,PartyType.ORGANISATION))
    }

    @Test
    fun `get count`(){
        val startDate = tenantDateTime.now().plusDays(-1).toDate()
        val endDate = tenantDateTime.now().toDate()
        val count = customerDetailService.getGroupByCustomerCount(DTOCustomerDetailQueryParams(
            startDate,endDate
        ))

        Assertions.assertThat(count[0].partyCount).isEqualTo(5)
        Assertions.assertThat(count[0].personCount).isEqualTo(2)
        Assertions.assertThat(count[0].organisationCount).isEqualTo(3)
    }

}