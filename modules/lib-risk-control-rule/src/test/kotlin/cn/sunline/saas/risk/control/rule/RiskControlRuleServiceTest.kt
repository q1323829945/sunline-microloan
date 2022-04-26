package cn.sunline.saas.risk.control.rule

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.risk.control.rule.modules.DataItem
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RelationalOperatorType
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleAdd
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleParam
import cn.sunline.saas.risk.control.rule.services.RiskControlRuleService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.PostConstruct

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RiskControlRuleServiceTest {
    @Autowired
    private lateinit var riskControlRuleService: RiskControlRuleService

    @PostConstruct
    fun setTenantId(){
        ContextUtil.setTenant("0")
    }

    @Test
    fun `add one`(){
        val dtoRiskControlRuleAdd = DTORiskControlRuleAdd(
            null,
            "testRule1",
            RuleType.BUSINESS,
            null,
            "测试",
            null,
            LogicalOperationType.AND,
            listOf(DTORiskControlRuleParam(null,null, DataItem.CREDIT_RISK, RelationalOperatorType.EQ,"123",LogicalOperationType.AND))
        )
        val result = riskControlRuleService.addRiskControlRule(dtoRiskControlRuleAdd)

        assertThat(result).isNotNull
    }
}