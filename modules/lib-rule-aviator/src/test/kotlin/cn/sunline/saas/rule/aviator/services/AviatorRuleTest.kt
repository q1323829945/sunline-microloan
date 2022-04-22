package cn.sunline.saas.rule.aviator.services

import cn.sunline.saas.rule.engine.api.RuleApi
import cn.sunline.saas.rule.engine.model.Condition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AviatorRuleTest {
    @Autowired
    private lateinit var aviatorRule: RuleApi

    @Test
    fun `check false rule`() {
        val map = HashMap<String, Any>()
        map["value1"] = 2
        map["value2"] = 5

        val conditions = ArrayList<String>()
        val condition1 = "value1 == 2"
        conditions.add(condition1)
        val condition2 = "value2 > 5"
        conditions.add(condition2)




        val result = aviatorRule.execute(map, conditions)

        assertThat(result.result).isEqualTo(false)
    }

    @Test
    fun `check true rule`() {
        val map = HashMap<String, Any>()
        map["value1"] = 2
        map["value2"] = 3

        val conditions = ArrayList<String>()
        val condition1 = "value1 == 2"
        conditions.add(condition1)
        val condition2 = "value2 < 5"
        conditions.add(condition2)

        val result = aviatorRule.execute(map, conditions)

        assertThat(result.result).isEqualTo(true)

    }

}

