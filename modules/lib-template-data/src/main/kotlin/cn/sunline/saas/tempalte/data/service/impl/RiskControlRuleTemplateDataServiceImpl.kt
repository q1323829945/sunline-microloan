package cn.sunline.saas.tempalte.data.service.impl

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.risk.control.rule.modules.DataItem
import cn.sunline.saas.risk.control.rule.modules.LogicalOperationType
import cn.sunline.saas.risk.control.rule.modules.RelationalOperatorType
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleParam
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.tempalte.data.service.TemplateDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses

@Service
class RiskControlRuleTemplateDataServiceImpl: TemplateDataService() {


    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime


    private fun getDTORiskControlRuleParam(): List<DTORiskControlRuleParam> {
        var listOf = mutableListOf<DTORiskControlRuleParam>()
        listOf += DTORiskControlRuleParam(
            id = null,
            ruleId = null,
            dataItem = DataItem.CREDIT_RISK,
            relationalOperatorType = RelationalOperatorType.EQ,
            threshold = "th_" + sequence.nextId().toString().substring(6, 9),
            logicalOperationType = LogicalOperationType.AND,
            tenantId = null
        )
        return listOf
    }


    override fun <T : Any> getTemplateData(
        type: KClass<T>,
        defaultMapData: Map<String, Any>?,
        overrideDefaults: Boolean
    ): T {
        val constructor = type.primaryConstructor!!
        val mapData = mutableMapOf<KParameter, Any?>()

        constructor.parameters.forEach { param ->

            if (param.type.classifier == Long::class) {
                mapData[param] = sequence.nextId()
            }
            if (param.type.classifier == String::class) {
                if (param.name!! == "id" || param.name!! == "tenantId") {
                    mapData[param] = null
                } else {
                    mapData[param] = "rc_" + sequence.nextId().toString().substring(6, 9)
                }
            }
            if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
            }

            if (param.name!! == "params") {
                mapData[param] = getDTORiskControlRuleParam()
            }
        }

        return constructor.callBy(mapData)

    }
}