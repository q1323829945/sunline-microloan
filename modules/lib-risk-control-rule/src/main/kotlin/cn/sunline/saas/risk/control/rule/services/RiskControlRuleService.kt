package cn.sunline.saas.risk.control.rule.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.risk.control.rule.exception.RiskControlRuleNotFoundException
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRule
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRuleParam
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleDetailGroup
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleView
import cn.sunline.saas.risk.control.rule.repositories.RiskControlRuleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate
import cn.sunline.saas.seq.Sequence
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

@Service
class RiskControlRuleService(private val riskControlRuleRepository: RiskControlRuleRepository):
    BaseMultiTenantRepoService<RiskControlRule,Long>(riskControlRuleRepository) {

    @Autowired
    private lateinit var sequence: Sequence

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun addRiskControlRule(riskControlRule: RiskControlRule):RiskControlRule{
        riskControlRule.id = sequence.nextId()
        riskControlRule.sort = getMaxSort(riskControlRule.ruleType) + 1
        riskControlRule.params.forEach {
            it.id = sequence.nextId()
            it.ruleId = riskControlRule.id
        }
        riskControlRule.description = setDescription(riskControlRule.params)

        return save(riskControlRule)
    }

    fun updateRiskControlRule(oldOne:RiskControlRule, newOne: RiskControlRule): RiskControlRule {

        newOne.params.forEach {
            it.id ?:run {
                it.id = sequence.nextId()
                it.ruleId = oldOne.id
            }
        }

        oldOne.name = newOne.name
        oldOne.remark = newOne.remark
        oldOne.logicalOperationType = newOne.logicalOperationType
        oldOne.params = newOne.params
        oldOne.description = setDescription(newOne.params)

        return save(oldOne)
    }

    fun deleteRiskControlRule(id:Long){
        val riskControlRule = this.getOne(id) ?: throw RiskControlRuleNotFoundException("Invalid risk control rule")
        riskControlRuleRepository.delete(riskControlRule)
    }

    fun getAllRiskControlRuleDetailSort(ruleType: RuleType):List<DTORiskControlRuleDetailGroup>{
        val spec = getSpec(ruleType)
        val sortOrder = Sort.by(Sort.Order.asc("sort"))
        val riskControlRuleList = riskControlRuleRepository.findAll(spec,sortOrder)

        return getParamsGroup(riskControlRuleList)
    }

    private fun getParamsGroup(inputList: List<RiskControlRule>):List<DTORiskControlRuleDetailGroup>{
        val outputList = mutableListOf<DTORiskControlRuleDetailGroup>()
        var lastLogicalOperationType:LogicalOperationType = LogicalOperationType.AND
        val params = mutableListOf<DTORiskControlRuleView>()

        inputList.forEach {
            if(lastLogicalOperationType != it.logicalOperationType){
                if(params.size != 0){
                    val lastLogicalOperationParams = mutableListOf<DTORiskControlRuleView>()
                    lastLogicalOperationParams.addAll(params)
                    outputList.add(
                        DTORiskControlRuleDetailGroup(
                            lastLogicalOperationType,
                            lastLogicalOperationParams
                        )
                    )
                    params.clear()
                }
                lastLogicalOperationType = it.logicalOperationType
            }
            params.add(objectMapper.convertValue(it))
        }

        if(params.size != 0){
            outputList.add(
                DTORiskControlRuleDetailGroup(
                    lastLogicalOperationType,
                    params
                )
            )
        }
        return outputList
    }

    private fun getMaxSort(ruleType: RuleType):Long{
        val spec = getSpec(ruleType)
        val sortOrder = Sort.by(Sort.Order.desc("sort"))
        val riskControlRule = riskControlRuleRepository.findAll(spec,sortOrder).firstOrNull()
        return riskControlRule?.sort?:0
    }

    private fun getSpec(ruleType: RuleType):Specification<RiskControlRule>{
        return Specification<RiskControlRule> { root, _, criteriaBuilder  ->
            val predicates = mutableListOf<Predicate>()
            predicates.add(criteriaBuilder.equal(root.get<Long>("tenantId"), ContextUtil.getTenant()))
            predicates.add(criteriaBuilder.equal(root.get<RuleType>("ruleType"), ruleType))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }
    }


    private fun setDescription(params:List<RiskControlRuleParam>?):String{
        val description = StringBuffer()

        var lastLogicalOperationType:LogicalOperationType? = null
        params?.run {
            for(i in this.indices){
                val param = this[i]
                if(param.logicalOperationType != lastLogicalOperationType){
                    lastLogicalOperationType = param.logicalOperationType
                    if(param.logicalOperationType == LogicalOperationType.OR){
                        description.append("(")
                    }
                }
                description.append("${param.dataItem.key} ${param.relationalOperatorType.symbol} ${param.threshold}")
                if(i < this.size-1 ){
                    if(param.logicalOperationType == LogicalOperationType.OR && params[i+1].logicalOperationType == LogicalOperationType.AND){
                        description.append(") ${LogicalOperationType.AND.symbol} ")
                    }else {
                        description.append(" ${param.logicalOperationType.symbol} ")
                    }
                }

                if(i == this.size -1 && param.logicalOperationType == LogicalOperationType.OR){
                    description.append(")")
                }
            }
        }
        return description.toString()
    }

}