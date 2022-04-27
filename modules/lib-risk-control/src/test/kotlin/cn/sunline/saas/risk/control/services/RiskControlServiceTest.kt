package cn.sunline.saas.risk.control.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRule
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleDetailGroup
import com.google.gson.Gson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.PostConstruct

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RiskControlServiceTest {

    @Autowired
    private lateinit var riskControlService: RiskControlService

    @PostConstruct
    fun setTenantId(){
        ContextUtil.setTenant("0")
    }

    @Test
    fun execute(){
        riskControlService.execute(1,RuleType.PERSONAL)
    }


}
