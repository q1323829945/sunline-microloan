package cn.sunline.saas.risk.control.rule.modules.dto

import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RuleType

data class DTORiskControlRuleView (
    val id:String,
    val name:String,
    val ruleType:RuleType,
    var sort:Long,
    val remark: String? = null,
    val description:String,
    val logicalOperationType:LogicalOperationType,
    val params:List<DTORiskControlRuleParam>? = null,
    val tenantId:String,
)

data class DTORiskControlRuleDetailGroup(
    val logicalOperationType: LogicalOperationType,
    val params:List<DTORiskControlRuleView>,
)