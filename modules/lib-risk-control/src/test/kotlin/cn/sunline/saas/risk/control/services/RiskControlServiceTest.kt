package cn.sunline.saas.risk.control.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.risk.control.rule.modules.DataItem
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RelationalOperatorType
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleAdd
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleParam
import cn.sunline.saas.risk.control.rule.services.RiskControlRuleService
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.PostConstruct

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RiskControlServiceTest {

    @Autowired
    private lateinit var riskControlRuleService: RiskControlRuleService

    @Autowired
    private lateinit var riskControlService: RiskControlService

    @PostConstruct
    fun setTenantId(){
        ContextUtil.setTenant("0")
    }

    @BeforeEach
    fun setList(){
        riskControlRuleService.addRiskControlRule(DTORiskControlRuleAdd(
            null,
            "rule1",
            RuleType.PERSONAL,
            null,
            null,
            null,
            LogicalOperationType.OR,
            listOf(DTORiskControlRuleParam(
                null,
                null,
                DataItem.CREDIT_RISK,
                RelationalOperatorType.LE,
                "123",
                LogicalOperationType.OR
            ),DTORiskControlRuleParam(
                null,
                null,
                DataItem.FRAUD_EVALUATION,
                RelationalOperatorType.LT,
                "456",
                LogicalOperationType.OR
            ))
        ))
    }

    @Test
    fun execute(){
        val result = riskControlService.execute(1,RuleType.PERSONAL)
        assertThat(result.result).isEqualTo(true)
    }


}
