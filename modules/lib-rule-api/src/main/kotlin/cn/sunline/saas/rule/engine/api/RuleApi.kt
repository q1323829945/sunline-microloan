package cn.sunline.saas.rule.engine.api


data class RuleResult(
        val result:Any,
        val reason:String? =  null
)

interface RuleApi {
    fun execute(data:Map<String,Any>,conditions: List<String>):RuleResult

}