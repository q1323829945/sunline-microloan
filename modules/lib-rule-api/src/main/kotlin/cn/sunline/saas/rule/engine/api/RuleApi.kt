package cn.sunline.saas.rule.engine.api

import cn.sunline.saas.rule.engine.model.Condition

data class RuleResult(
        val result:Any,
        val reason:String
)

interface RuleApi {
    fun execute(data:Map<String,Any>,conditions: List<Condition>):RuleResult

}