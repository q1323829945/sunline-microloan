package cn.sunline.saas.channel.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.global.util.setTimeZone
import cn.sunline.saas.channel.rbac.modules.dto.DTOCustomerAdd
import cn.sunline.saas.channel.rbac.services.CustomerService
import org.assertj.core.api.Assertions
import org.joda.time.DateTimeZone
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CustomerServiceTest {
    @Autowired
    private lateinit var customerService: CustomerService

    @BeforeAll
    fun init(){
        ContextUtil.setTimeZone(DateTimeZone.UTC)
        ContextUtil.setTenant("12344566")

        customerService.addOne(
            DTOCustomerAdd(
                username = "",
                userId = "123"
            )
        )
    }

    @Test
    fun `get one`(){
        val customer = customerService.findByUserId("123")

        Assertions.assertThat(customer).isNotNull
    }


}