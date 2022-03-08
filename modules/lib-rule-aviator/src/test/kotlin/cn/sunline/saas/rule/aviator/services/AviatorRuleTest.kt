package cn.sunline.saas.rule.aviator.services

import cn.sunline.saas.rule.engine.api.RuleApi
import cn.sunline.saas.rule.engine.model.Condition
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AviatorRuleTest {
    @Autowired
    private lateinit var aviatorRule: RuleApi

    @Test
    fun `check false rule`(){
        val map = HashMap<String,Any>()
        map["value1"] = 2
        map["value2"] = 5

        val conditions = ArrayList<Condition>()
        conditions.add(Condition(null,"equals","value1 == 2"))
        conditions.add(Condition(null,"equals","value2 < 4"))


        val result = aviatorRule.execute(map,conditions)

        assertThat(result.result).isEqualTo(false)
        assertThat(result.reason).isEqualTo("(2 == 2) && (5 < 4)")

    }

    @Test
    fun `check true rule`(){
        val map = HashMap<String,Any>()
        map["value1"] = 2
        map["value2"] = 3

        val conditions = ArrayList<Condition>()
        conditions.add(Condition(null,"equals","value1 == 2"))
        conditions.add(Condition(null,"equals","value2 < 4"))


        val result = aviatorRule.execute(map,conditions)

        assertThat(result.result).isEqualTo(true)
        assertThat(result.reason).isEqualTo("(2 == 2) && (3 < 4)")

    }

    @Test
    fun `check custom multiply rule`(){
        val map = HashMap<String,Any>()
        map["value1"] = 3
        map["value2"] = 4
        val conditions = ArrayList<Condition>()
        conditions.add(Condition(null,"multiply","multiply(value1,value2)"))
        val result = aviatorRule.execute(map,conditions)

        assertThat(result.result).isEqualTo(12.0)
        assertThat(result.reason).isEqualTo("(multiply(3,4))")
    }

}

