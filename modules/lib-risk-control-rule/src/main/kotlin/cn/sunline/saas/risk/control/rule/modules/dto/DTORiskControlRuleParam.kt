package cn.sunline.saas.risk.control.rule.modules.dto

import cn.sunline.saas.risk.control.rule.modules.DataItem
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RelationalOperatorType


data class DTORiskControlRuleParam(
    var id:String?,
    var ruleId:String?,
    val dataItem: DataItem,
    val relationalOperatorType: RelationalOperatorType,
    val threshold:String,
    var logicalOperationType: LogicalOperationType?,
    var tenantId:Long? = null,
)
