package cn.sunline.saas.risk.control.services

import cn.sunline.saas.risk.control.datasource.factory.DataSourceFactory
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
        val reason:String? = null
    )


    fun execute(customerId:Long,rules:List<RiskControlRule>): ExecuteResult{
        //TODO:

        rules.forEach {  riskControlRule ->
            val map = mutableMapOf<String,Number>()
            val conditions = mutableListOf<String>()
            conditions.add(riskControlRule.description!!)
            riskControlRule.params.forEach {
                map[it.dataSourceType.name] = DataSourceFactory.instance(it.dataSourceType).calculation(customerId)
            }
            val result = ruleApi.execute(map,conditions)
            if(result.result == false){
                return ExecuteResult(
                    false,
                    "${riskControlRule.name} fail ,because ${riskControlRule.description} -> ${result.reason}"
                )
            }
        }

        return ExecuteResult(true)
    }


}
