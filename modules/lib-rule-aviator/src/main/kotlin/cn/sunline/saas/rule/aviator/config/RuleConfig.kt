package cn.sunline.saas.rule.aviator.config

import cn.sunline.saas.rule.aviator.function.MultiplyFunction
import com.googlecode.aviator.AviatorEvaluator
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class RuleConfig {
    @PostConstruct
    fun initFunction(){
        //add function from cn.sunline.saas.rule.aviator.function class
        AviatorEvaluator.addFunction(MultiplyFunction())
    }
}