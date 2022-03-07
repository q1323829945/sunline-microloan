package cn.sunline.saas.rule.aviator.config

import cn.sunline.saas.rule.aviator.function.MultiplyFunction
import com.googlecode.aviator.AviatorEvaluator
import org.springframework.boot.test.context.TestComponent
import org.springframework.boot.test.context.TestConfiguration
import javax.annotation.PostConstruct

@TestConfiguration
class RuleConfigTest {
    @PostConstruct
    fun initFunction(){
        //add function from cn.sunline.saas.rule.aviator.function class
        AviatorEvaluator.addFunction(MultiplyFunction())
    }
}