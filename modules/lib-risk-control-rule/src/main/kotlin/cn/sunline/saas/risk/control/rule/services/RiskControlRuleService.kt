package cn.sunline.saas.risk.control.rule.services

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.risk.control.rule.exception.RiskControlRuleNotFoundException
import cn.sunline.saas.risk.control.rule.modules.DataItem
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RelationalOperatorType
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRule
import cn.sunline.saas.risk.control.rule.modules.db.RiskControlRuleParam
import cn.sunline.saas.risk.control.rule.modules.dto.*
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
import com.google.gson.Gson
import javax.transaction.Transactional

@Service
class RiskControlRuleService(private val riskControlRuleRepository: RiskControlRuleRepository):
    BaseMultiTenantRepoService<RiskControlRule,Long>(riskControlRuleRepository) {

    @Autowired
    private lateinit var sequence: Sequence


    @Autowired
    private lateinit var riskControlRuleParamService: RiskControlRuleParamService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    fun addRiskControlRule(dtoRiskControlRuleAdd: DTORiskControlRuleAdd):DTORiskControlRuleView{
        dtoRiskControlRuleAdd.id = sequence.nextId().toString()
        dtoRiskControlRuleAdd.sort = getMaxSort(dtoRiskControlRuleAdd.ruleType) + 1

        dtoRiskControlRuleAdd.params?.forEach {
            it.id = sequence.nextId().toString()
            it.ruleId = dtoRiskControlRuleAdd.id
        }

        val riskControlRule = objectMapper.convertValue<RiskControlRule>(dtoRiskControlRuleAdd)

        riskControlRule.description = setDescription(riskControlRule.params)

        val save = save(riskControlRule)
        val result = objectMapper.convertValue<DTORiskControlRuleView>(save)
        result.group = getDTORiskControlRuleGroup(save.params)

        return result
    }

    fun riskControlRuleSort(dtoRiskControlRuleViewList:List<DTORiskControlRuleView>){
        for(i in dtoRiskControlRuleViewList.indices){
            dtoRiskControlRuleViewList[i].sort = (i+1).toLong()
        }
        val iterable = objectMapper.convertValue<List<RiskControlRule>>(dtoRiskControlRuleViewList)
        this.save(iterable)
    }


    fun getDetail(id:Long): DTORiskControlRuleView {
        val riskControlRule = this.getOne(id) ?: throw RiskControlRuleNotFoundException("Invalid risk control rule")

        val result = objectMapper.convertValue<DTORiskControlRuleView>(riskControlRule)
        result.group = getDTORiskControlRuleGroup(riskControlRule.params)

        return result
    }


    @Transactional
    fun updateRiskControlRule(id: Long, dtoRiskControlRuleChange: DTORiskControlRuleChange): DTORiskControlRuleView {
        val oldOne = this.getOne(id) ?: throw RiskControlRuleNotFoundException("Invalid risk control rule")

        dtoRiskControlRuleChange.params?.forEach {
            it.id ?:run {
                it.id = sequence.nextId().toString()
                it.ruleId = oldOne.id.toString()
            }
        }

        val newOne = objectMapper.convertValue<RiskControlRule>(dtoRiskControlRuleChange)

        oldOne.name = newOne.name
        oldOne.remark = newOne.remark
        oldOne.logicalOperationType = newOne.logicalOperationType
        oldOne.params = newOne.params
        oldOne.description = setDescription(newOne.params)
        val save = this.save(oldOne)

        riskControlRuleParamService.deleteByRuleId(null)

        val result = objectMapper.convertValue<DTORiskControlRuleView>(save)
        result.group = getDTORiskControlRuleGroup(save.params)

        return result
    }

    fun deleteRiskControlRule(id:Long){
        val riskControlRule = this.getOne(id) ?: throw RiskControlRuleNotFoundException("Invalid risk control rule")
        riskControlRuleRepository.delete(riskControlRule)
    }


    fun getAllRiskControlRuleDetailSort(ruleType: RuleType):List<DTORiskControlRuleDetailGroup>{
        val spec = getSpec(ruleType)
        val sortOrder = Sort.by(Sort.Order.asc("sort"))
        val riskControlRuleList = riskControlRuleRepository.findAll(spec,sortOrder)

        val inputDataList = riskControlRuleList.map {
            InputData(
                it.logicalOperationType,
                it
            )
        }
        return objectMapper.convertValue(getParamsGroup<RiskControlRule>(inputDataList))


    }

    private fun getDTORiskControlRuleGroup(riskControlRuleParamList: List<RiskControlRuleParam>):List<DTORiskControlRuleParamGroup>{
        val inputDataList = riskControlRuleParamList.map {
            InputData(
                it.logicalOperationType,
                it
            )
        }

        val outputDataList = getParamsGroup<DTORiskControlRuleParam>(inputDataList)

        return objectMapper.convertValue(outputDataList)
    }

    private data class OutputData<T>(
        var logicalOperationType: LogicalOperationType?,
        val params:List<T>,
    )

    private data class InputData(
        var logicalOperationType: LogicalOperationType,
        var data:Any
    )

    /**
     *  inputList
     *  [
     *    {"logicalOperationType":"AND","data":{"id":1,"logicalOperationType":"AND",....}},
     *    {"logicalOperationType":"AND","data":{"id":2,"logicalOperationType":"AND",....}},
     *    {"logicalOperationType":"OR","data":{"id":3,"logicalOperationType":"OR",....}},
     *    {"logicalOperationType":"OR","data":{"id":4,"logicalOperationType":"OR",....}},
     *    {"logicalOperationType":"OR","data":{"id":5,"logicalOperationType":"AND",....}},
     *    {"logicalOperationType":"OR","data":{"id":6,"logicalOperationType":"AND",....}},
     *  ]
     *
     *  outputData
     *  [
     *      {"logicalOperationType":"AND",
     *       "params":[{"id":1,"logicalOperationType":"AND",...},
     *                 {"id":2,"logicalOperationType":"AND",...}]
     *      },
     *      {"logicalOperationType":"OR",
     *       "params":[{"id":3,"logicalOperationType":"OR",...}]
     *       "params":[{"id":4,"logicalOperationType":"OR",...}]
     *      },
     *      {"logicalOperationType":"AND",
     *       "params":[{"id":5,"logicalOperationType":"AND",...},
     *                 {"id":6,"logicalOperationType":"AND",...}]
     *      }
     *  ]
     */
    private inline fun <reified T> getParamsGroup(inputList: List<InputData>):List<OutputData<T>>{
        val outputList = mutableListOf<OutputData<T>>()
        var lastLogicalOperationType:LogicalOperationType? = null
        val params = mutableListOf<T>()

        inputList.forEach {
            if(lastLogicalOperationType != it.logicalOperationType){
                if(params.size != 0){
                    val lastLogicalOperationParams = mutableListOf<T>()
                    lastLogicalOperationParams.addAll(params)
                    outputList.add(
                        OutputData(
                            lastLogicalOperationType,
                            lastLogicalOperationParams
                        )
                    )
                    params.clear()
                }
                lastLogicalOperationType = it.logicalOperationType
            }
            params.add(objectMapper.convertValue(it.data))
        }

        if(params.size != 0){
            outputList.add(
                OutputData(
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

        var lastLogicalOperationType:LogicalOperationType = LogicalOperationType.AND
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

//fun main() {
//    val l= mutableListOf<RiskControlRuleParam>(
//        RiskControlRuleParam(
//            1,
//            1,
//            DataItem.FRAUD_EVALUATION,
//            RelationalOperatorType.LE,
//            "123",
//            LogicalOperationType.AND
//        ),
//        RiskControlRuleParam(
//            1,
//            1,
//            DataItem.FRAUD_EVALUATION,
//            RelationalOperatorType.LE,
//            "123",
//            LogicalOperationType.OR
//        ),
//        RiskControlRuleParam(
//            1,
//            1,
//            DataItem.FRAUD_EVALUATION,
//            RelationalOperatorType.LE,
//            "123",
//            LogicalOperationType.OR
//        ),
//        RiskControlRuleParam(
//            1,
//            1,
//            DataItem.FRAUD_EVALUATION,
//            RelationalOperatorType.LE,
//            "123",
//            LogicalOperationType.AND
//        )
//    )
//
//    val input = l.map {
//        InputData(
//            it.logicalOperationType,
//            it
//        )
//    }
//
//    println(Gson().toJson(getParamsGroup(input,mutableListOf<DTORiskControlRuleParam>())))
//
//}
//
//
//
//
//private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)