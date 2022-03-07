package cn.sunline.saas.rule.aviator.services

import cn.sunline.saas.rule.aviator.modules.RuleParams
import cn.sunline.saas.rule.engine.api.RuleApi
import cn.sunline.saas.rule.engine.api.RuleResult
import cn.sunline.saas.rule.engine.model.Condition
import com.googlecode.aviator.AviatorEvaluator
import org.springframework.stereotype.Service

@Service
class AviatorRule: RuleApi {
    override fun execute(data: Any, condition: Condition): RuleResult {
        val params = data as List<RuleParams>
        val map = HashMap<String,Any>()
        params.forEach{
            map[it.key] = it.value
        }
        var reason = condition.description

        map.forEach{
            reason = reason.replace(it.key,it.value.toString())
        }

        return RuleResult(AviatorEvaluator.execute(condition.description,map),reason)
    }

}