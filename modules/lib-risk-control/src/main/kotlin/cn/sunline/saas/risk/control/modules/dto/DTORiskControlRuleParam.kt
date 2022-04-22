package cn.sunline.saas.risk.control.modules.dto

import cn.sunline.saas.risk.control.modules.DataSourceType
import cn.sunline.saas.risk.control.modules.RelationalOperatorType


data class DTORiskControlRuleParam(
    var id:String?,
    var ruleId:String?,
    val dataSourceType: DataSourceType,
    val relationalOperatorType: RelationalOperatorType,
    val threshold:String,
)
