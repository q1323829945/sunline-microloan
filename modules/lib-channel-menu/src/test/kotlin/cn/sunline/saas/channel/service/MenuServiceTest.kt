package cn.sunline.saas.channel.service

import cn.sunline.saas.channel.menu.services.MenuService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuServiceTest {
    @Autowired
    private lateinit var menuService: MenuService

    @Test
    fun `get menu`(){
        val menuNames = mutableSetOf("businessConfig","sysConfig","enterpriseConfig","businessHandling"
            ,"roleConfig"
            ,"permissionConfig"
            ,"userConfig"
            ,"ratePlanConfig"
            ,"documentTemplateConfig"
            ,"loanProductConfig"
            ,"riskControlRuleConfig"
            ,"organisationConfig"
            ,"customerConfig"
            ,"customerOfferConfig"
            ,"loanAgreementManagementConfig"
            ,"employeeConfig"
            ,"repaymentManagementConfig")

        val menus = menuService.getPermissionMenu(
            menuNames
        )

        Assertions.assertThat(menus).isNotNull
    }

}