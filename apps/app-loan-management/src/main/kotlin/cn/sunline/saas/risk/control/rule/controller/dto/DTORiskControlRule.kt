package cn.sunline.saas.risk.control.rule.controller.dto

import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleParam

data class DTORiskControlRuleAdd (
    var id:String?,
    val name:String,
    val ruleType: RuleType,
    var sort:Long? = null,
    val remark: String? = null,
    val description:String? = null,
    val logicalOperationType: LogicalOperationType,
    val params:List<DTORiskControlRuleParam>,
    var tenantId:Long? = null,
)

data class DTORiskControlRuleChange (
    val name:String,
    val ruleType:RuleType,
    val sort:Long? = null,
    val remark: String? = null,
    val logicalOperationType:LogicalOperationType,
    val params:List<DTORiskControlRuleParam>,
)

data class DTORiskControlRuleListView(
    val id:String,
    val name:String,
    val ruleType: RuleType,
    val remark: String? = null,
    val logicalOperationType: LogicalOperationType,
    val description:String
)

data class DTORiskControlRuleGroup(
    val logicalOperationType: LogicalOperationType,
    val params:List<DTORiskControlRuleListView>,
)
