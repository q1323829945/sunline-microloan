package cn.sunline.saas.risk.control.rule.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.risk.control.rule.exception.RiskControlRuleNotFoundException
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRule
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRuleParam
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleAdd
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleChange
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
import javax.transaction.Transactional

@Service
class RiskControlRuleService(private val riskControlRuleRepository: RiskControlRuleRepository):
    BaseMultiTenantRepoService<RiskControlRule,Long>(riskControlRuleRepository) {

    @Autowired
    private lateinit var sequence: Sequence


    @Autowired
    private lateinit var riskControlRuleParamService: RiskControlRuleParamService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun addRiskControlRule(dtoRiskControlRuleAdd: DTORiskControlRuleAdd):RiskControlRule{
        dtoRiskControlRuleAdd.id = sequence.nextId().toString()
        dtoRiskControlRuleAdd.sort = getMaxSort(dtoRiskControlRuleAdd.ruleType) + 1

        dtoRiskControlRuleAdd.params?.forEach {
            it.id = sequence.nextId().toString()
            it.ruleId = dtoRiskControlRuleAdd.id
        }

        val riskControlRule = objectMapper.convertValue<RiskControlRule>(dtoRiskControlRuleAdd)

        riskControlRule.description = setDescription(riskControlRule.params)

        return save(riskControlRule)
    }


    fun riskControlRuleSort(dtoRiskControlRuleViewList:List<DTORiskControlRuleView>){

        for(i in dtoRiskControlRuleViewList.indices){
            dtoRiskControlRuleViewList[i].sort = (i+1).toLong()
        }

        val iterable = objectMapper.convertValue<List<RiskControlRule>>(dtoRiskControlRuleViewList)

        this.save(iterable)
    }


    fun getDetail(id:Long): RiskControlRule {
        return this.getOne(id) ?: throw RiskControlRuleNotFoundException("Invalid risk control rule")
    }


    @Transactional
    fun updateRiskControlRule(id: Long, dtoRiskControlRuleChange: DTORiskControlRuleChange): RiskControlRule {
        val oldOne = getDetail(id)

        dtoRiskControlRuleChange.params?.forEach {
            it.id ?: run {
                it.id = sequence.nextId().toString()
                it.ruleId = oldOne.id.toString()
            }
        }

        val newOne = objectMapper.convertValue<RiskControlRule>(dtoRiskControlRuleChange)

        oldOne.name = newOne.name
        oldOne.remark = newOne.remark
        oldOne.params = newOne.params
        oldOne.description = setDescription(newOne.params)

        val save = this.save(oldOne)

        riskControlRuleParamService.deleteByRuleId(null)

        return save
    }

    @Transactional
    fun deleteRiskControlRule(id:Long){
        val riskControlRule = getDetail(id)
        riskControlRuleRepository.delete(riskControlRule)
    }

    fun getAllControlRuleSort(ruleType: RuleType):List<RiskControlRule>{
        val spec = getSpec(ruleType)
        val sortOrder = Sort.by(Sort.Order.asc("sort"))
        return riskControlRuleRepository.findAll(spec,sortOrder)
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

        params?.run {
            for(i in this.indices){
                val param = this[i]
                description.append("${param.dataSourceType} ${param.relationalOperatorType.symbol} ${param.threshold}")
                if(i < this.size-1){
                    description.append(" && ")
                }
            }
        }

        return description.toString()

    }

}
