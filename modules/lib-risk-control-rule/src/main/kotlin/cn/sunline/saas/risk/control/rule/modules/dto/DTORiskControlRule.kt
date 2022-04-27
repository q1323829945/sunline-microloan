package cn.sunline.saas.risk.control.rule.modules.dto

import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRule


data class DTORiskControlRuleAdd (
    var id:String?,
    val name:String,
    val ruleType: RuleType,
    var sort:Long? = null,
    val remark: String? = null,
    val description:String? = null,
    val logicalOperationType:LogicalOperationType,
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
    val logicalOperationType:LogicalOperationType,
    val params:List<DTORiskControlRuleParam>? = null,
    val tenantId:String,
)

data class DTORiskControlRuleChange (
    val name:String,
    val ruleType:RuleType,
    val sort:Long? = null,
    val remark: String? = null,
    val logicalOperationType:LogicalOperationType,
    val params:List<DTORiskControlRuleParam>? = null,
)

data class DTORiskControlRuleGroup(
    val logicalOperationType: LogicalOperationType,
    val params:List<DTORiskControlRuleListView>,
)

data class DTORiskControlRuleListView(
    val id:String,
    val name:String,
    val ruleType: RuleType,
    val remark: String? = null,
    val logicalOperationType:LogicalOperationType,
    val description:String
)

data class DTORiskControlRuleDetailGroup(
    val logicalOperationType: LogicalOperationType,
    val params:List<DTORiskControlRuleView>,
)


data class DTORiskControlRuleParamGroup(
    var logicalOperationType: LogicalOperationType?,
    val params:List<DTORiskControlRuleParam>,
)

data class DTORiskControlRuleSort(
    val sortList:List<DTORiskControlRuleView>
)