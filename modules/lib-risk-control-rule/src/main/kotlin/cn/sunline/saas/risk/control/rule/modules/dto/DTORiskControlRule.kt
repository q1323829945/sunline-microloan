package cn.sunline.saas.risk.control.rule.modules.dto

import cn.sunline.saas.risk.control.rule.modules.RuleType


data class DTORiskControlRuleAdd (
    var id:String?,
    val name:String,
    val ruleType: RuleType,
    var sort:Long? = null,
    val remark: String? = null,
    val description:String? = null,
    val params:List<DTORiskControlRuleParam>? = null,
    val tenantId:String? = null,
)

data class DTORiskControlRuleView (
    val id:String,
    val name:String,
    val ruleType:RuleType,
    var sort:Long,
    val remark: String? = null,
    val description:String? = null,
    val params:List<DTORiskControlRuleParam>? = null,
    val tenantId:String,

)

data class DTORiskControlRuleChange (
    val name:String,
    val ruleType:RuleType,
    val sort:Long? = null,
    val remark: String? = null,
    val params:List<DTORiskControlRuleParam>? = null,
)

data class DTORiskControlRuleSort(
    val sortList:List<DTORiskControlRuleView>
)