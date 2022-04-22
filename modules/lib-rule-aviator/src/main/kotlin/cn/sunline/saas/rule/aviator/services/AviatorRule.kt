package cn.sunline.saas.rule.aviator.services

import cn.sunline.saas.rule.engine.api.RuleApi
import cn.sunline.saas.rule.engine.api.RuleResult
import cn.sunline.saas.rule.engine.model.Condition
import com.googlecode.aviator.AviatorEvaluator
import org.springframework.stereotype.Service

@Service
class AviatorRule: RuleApi {

    override fun execute(data: Map<String, Any>, conditions: List<String>): RuleResult {

        val reasonStringBuffer = StringBuffer("(")
        for(idx in conditions.indices){
            reasonStringBuffer.append(conditions[idx])
            if(idx != conditions.size -1){
                reasonStringBuffer.append(") && (")
            }
        }
        reasonStringBuffer.append(")")

        var reason = reasonStringBuffer.toString()

        data.forEach{
            reason = reason.replace(it.key,it.value.toString())
        }

        return RuleResult(AviatorEvaluator.execute(reasonStringBuffer.toString(),data),reason)
    }


}