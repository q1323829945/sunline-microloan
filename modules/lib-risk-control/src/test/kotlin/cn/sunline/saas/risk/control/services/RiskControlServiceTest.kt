package cn.sunline.saas.risk.control.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.risk.control.rule.modules.DataItem
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RelationalOperatorType
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRule
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRuleParam
import cn.sunline.saas.risk.control.rule.services.RiskControlRuleService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RiskControlServiceTest {

    @Autowired
    private lateinit var riskControlRuleService: RiskControlRuleService

    @Autowired
    private lateinit var riskControlService: RiskControlService

    @BeforeEach
    fun setTenantId(){
        ContextUtil.setTenant("0")
    }

    @BeforeEach
    fun setList(){
        val riskControlRule = RiskControlRule(
            null,
            "rule1",
            RuleType.PERSONAL,
            null,
            LogicalOperationType.OR,
            null,
            null
        )
        riskControlRule.params = mutableListOf(
                                        RiskControlRuleParam(
                                        null,
                                        null,
                                        DataItem.CREDIT_RISK,
                                        RelationalOperatorType.LE,
                                        "123",
                                        LogicalOperationType.OR
                                    ),RiskControlRuleParam(
                                        null,
                                        null,
                                        DataItem.FRAUD_EVALUATION,
                                        RelationalOperatorType.LT,
                                        "456",
                                        LogicalOperationType.OR
                                    ))

        riskControlRuleService.addRiskControlRule(riskControlRule)
    }

    @Test
    fun execute(){
        val result = riskControlService.execute(1,RuleType.PERSONAL)
        assertThat(result.result).isEqualTo(true)
    }


}
