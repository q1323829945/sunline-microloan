package cn.sunline.saas.risk.control.services

import cn.sunline.saas.risk.control.datasource.factory.DataSourceFactory
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRule
import cn.sunline.saas.risk.control.rule.services.RiskControlRuleService
import cn.sunline.saas.rule.engine.api.RuleApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RiskControlService {
    @Autowired
    private lateinit var ruleApi:RuleApi

    @Autowired
    private lateinit var riskControlRuleService: RiskControlRuleService

    data class ExecuteResult(
        val result:Boolean,
        val reason:String? = null,
        val reports:List<Report>? = null
    )

    data class Report(
        val ruleName:String,
        val logicalOperationType: LogicalOperationType,
        val result: Boolean,
        val executeEquation:String
    )

    fun execute(customerId:Long,ruleType: RuleType): ExecuteResult{
        val groups = riskControlRuleService.getAllRiskControlRuleDetailSort(ruleType)
        val reports = mutableListOf<Report>()
        val condition = StringBuffer()

        for(i in groups.indices){
            val group = groups[i]

            if(group.logicalOperationType == LogicalOperationType.OR){
                condition.append(" (")
            }

            for(j in group.params.indices){
                val rule = group.params[j]
                val map = mutableMapOf<String,Number>()
                val conditions = mutableListOf<String>()
                conditions.add(rule.description!!)
                rule.params.forEach { param ->
                    map[param.dataItem.key] = DataSourceFactory.instance(param.dataItem).calculation(customerId)
                }
                val result = ruleApi.execute(map,conditions)

                reports.add(
                    Report(
                        rule.name,
                        rule.logicalOperationType,
                        result.result as Boolean,
                        "${rule.description!!} -> ${result.reason}"
                    )
                )
                condition.append(" ${result.result} ")
                if (j < group.params.size -1){
                    condition.append(rule.logicalOperationType.symbol)
                }
            }

            if(group.logicalOperationType == LogicalOperationType.OR){
                condition.append(") ")
            }

            if(i < groups.size -1){
                condition.append(LogicalOperationType.AND.symbol)
            }

        }
        val result = ruleApi.execute(mapOf(), mutableListOf(condition.toString()))
        return ExecuteResult(result.result as Boolean,result.reason,reports)
    }
}

