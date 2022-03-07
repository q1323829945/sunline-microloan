package cn.sunline.saas.rule.aviator.services

import cn.sunline.saas.rule.aviator.function.MultiplyFunction
import cn.sunline.saas.rule.aviator.modules.RuleParams
import cn.sunline.saas.rule.engine.model.Condition
import com.googlecode.aviator.AviatorEvaluator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import javax.annotation.PostConstruct


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AviatorRuleTest {
    @Autowired
    private lateinit var aviatorRule: AviatorRule

    @Test
    fun `check equals rule`(){
        val paramsList = ArrayList<RuleParams>()
        paramsList.add(RuleParams("value1",3))
        paramsList.add(RuleParams("value2",3))

        val condition = Condition(null,"equals","value1 == value2")

        val result = aviatorRule.execute(paramsList,condition)

        assertThat(result.result).isEqualTo(true)
        assertThat(result.reason).isEqualTo("3 == 3")

    }

    @Test
    fun `check gt rule`(){
        val paramsList = ArrayList<RuleParams>()
        paramsList.add(RuleParams("value1",3))

        val condition = Condition(null,"gt","value1 > 4")

        val result = aviatorRule.execute(paramsList,condition)

        assertThat(result.result).isEqualTo(false)
        assertThat(result.reason).isEqualTo("3 > 4")
    }

    @Test
    fun `check custom multiply rule`(){
        val paramsList = ArrayList<RuleParams>()
        paramsList.add(RuleParams("value1",3))
        paramsList.add(RuleParams("value2",4))
        val condition = Condition(null,"multiply","multiply(value1,value2)")

        val result = aviatorRule.execute(paramsList,condition)

        assertThat(result.result).isEqualTo(12.0)
        assertThat(result.reason).isEqualTo("multiply(3,4)")
    }

    @Test
    fun `check mix expression rule`(){
        val paramsList = ArrayList<RuleParams>()
        paramsList.add(RuleParams("a",3))
        paramsList.add(RuleParams("b",4))
        paramsList.add(RuleParams("c",5))
        val condition = Condition(null,"multiply","a + b > c")

        val result = aviatorRule.execute(paramsList,condition)

        assertThat(result.result).isEqualTo(true)
        assertThat(result.reason).isEqualTo("3 + 4 > 5")
    }
}

