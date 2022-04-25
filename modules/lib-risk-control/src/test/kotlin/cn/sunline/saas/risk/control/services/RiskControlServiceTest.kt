package cn.sunline.saas.risk.control.services

import cn.sunline.saas.risk.control.rule.modules.DataSourceType
import cn.sunline.saas.risk.control.rule.modules.RelationalOperatorType
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRule
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRuleParam
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RiskControlServiceTest {

    @Autowired
    private lateinit var riskControlService: RiskControlService

    @Test
    fun execute(){
        val list = getList()

        val result = riskControlService.execute(1,list)

        assertThat(result.result).isEqualTo(false)

    }

    private fun getList():List<RiskControlRule>{
        return mutableListOf(
            RiskControlRule(
                1,
                "第二条规则",
                RuleType.TYPE1,
                1,
                "",
                "SOURCE1 < 123",
                params = mutableListOf(
                    RiskControlRuleParam(
                    1,
                    2,
                    DataSourceType.SOURCE1,
                    RelationalOperatorType.LT,
                    "123"
                )
                )
            ),RiskControlRule(
                2,
                "第二条规则",
                RuleType.TYPE1,
                1,
                "",
                "SOURCE2 < 123",
                params = mutableListOf(RiskControlRuleParam(
                    2,
                    2,
                    DataSourceType.SOURCE2,
                    RelationalOperatorType.LT,
                    "123"
                ))
            ),RiskControlRule(
                3,
                "第二条规则",
                RuleType.TYPE1,
                1,
                "",
                "SOURCE3 < 123",
                params = mutableListOf(RiskControlRuleParam(
                    3,
                    3,
                    DataSourceType.SOURCE3,
                    RelationalOperatorType.LT,
                    "123"
                ))
            )
        )
    }
}