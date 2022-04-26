package cn.sunline.saas.risk.control.services

import cn.sunline.saas.risk.control.datasource.factory.DataSourceFactory
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRule
import cn.sunline.saas.rule.engine.api.RuleApi
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RiskControlService {
    @Autowired
    private lateinit var ruleApi:RuleApi

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


    fun execute(customerId:Long,rules:List<RiskControlRule>): ExecuteResult{
        //TODO:
        val reports = mutableListOf<Report>()

        rules.forEach {  riskControlRule ->
            val map = mutableMapOf<String,Number>()
            val conditions = mutableListOf<String>()
            conditions.add(riskControlRule.description!!)
            riskControlRule.params.forEach {
                map[it.dataItem.key] = DataSourceFactory.instance(it.dataItem).calculation(customerId)
            }
            val result = ruleApi.execute(map,conditions)

            reports.add(
                Report(
                    riskControlRule.name,
                    riskControlRule.logicalOperationType,
                    result.result as Boolean,
                    "${riskControlRule.description!!} -> ${result.reason}"
                )
            )
        }

        val conditions = mutableListOf<String>()
        val condition = StringBuffer()

        var lastLogicalOperationType:LogicalOperationType = LogicalOperationType.AND
        for(i in reports.indices){
            val report = reports[i]
            if(report.logicalOperationType != lastLogicalOperationType){
                lastLogicalOperationType = report.logicalOperationType
                if(report.logicalOperationType == LogicalOperationType.OR){
                    condition.append("(")
                }
            }
            condition.append(" ${report.result} ")
            if(i < reports.size-1 ){
                if(report.logicalOperationType == LogicalOperationType.OR && reports[i+1].logicalOperationType == LogicalOperationType.AND){
                    condition.append(") ${LogicalOperationType.AND.symbol} ")
                }else {
                    condition.append(" ${report.logicalOperationType.symbol} ")
                }
            }
            if(i == reports.size -1 && report.logicalOperationType == LogicalOperationType.OR){
                condition.append(")")
            }
        }

        conditions.add(condition.toString())

        val result = ruleApi.execute(mapOf(),conditions)



        return ExecuteResult(result.result as Boolean,result.reason,reports)
    }


}

