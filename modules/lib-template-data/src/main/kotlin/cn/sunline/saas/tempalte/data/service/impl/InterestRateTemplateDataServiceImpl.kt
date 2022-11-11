package cn.sunline.saas.tempalte.data.service.impl

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.interest.service.RatePlanService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.tempalte.data.exception.TemplateDataBusinessException
import cn.sunline.saas.tempalte.data.service.TemplateDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses


@Service
class InterestRateTemplateDataServiceImpl : TemplateDataService() {

    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    @Autowired
    private lateinit var ratePlanService: RatePlanService


    fun <T : Any> getTemplateData(type: KClass<T>, ratePlanId: String, overrideDefaults: Boolean): T {
        val ratePlan =
            ratePlanService.getOne(ratePlanId.toLong()) ?: throw TemplateDataBusinessException("Invalid RatePlan")
        val map = mutableMapOf<String, Any>()
        when (ratePlan.type) {
            RatePlanType.STANDARD,
            RatePlanType.CUSTOMER,
            RatePlanType.LOAN_TERM_TIER_CUSTOMER -> {
                map["toPeriod"] = LoanTermType.ONE_MONTH
                map["ratePlanId"] = ratePlanId
            }

            RatePlanType.LOAN_AMOUNT_TIER_CUSTOMER -> {
                map["toAmountPeriod"] = BigDecimal(100000)
                map["ratePlanId"] = ratePlanId
            }
        }
        map["rate"] = BigDecimal(0.2)
        return getTemplateData(type, map, overrideDefaults)
    }

    override fun <T : Any> getTemplateData(
        type: KClass<T>,
        defaultMapData: Map<String, Any>?,
        overrideDefaults: Boolean
    ): T {
        val constructor = type.primaryConstructor!!
        val mapData = mutableMapOf<KParameter, Any?>()
        constructor.parameters.forEach { param ->
            if(defaultMapData != null && defaultMapData.containsKey(param.name!!)){
                mapData[param] = defaultMapData[param.name!!]
            }
            if(!param.isOptional){
                if (param.type.classifier == Long::class) {
                    mapData[param] = sequence.nextId()
                }
                if (param.type.classifier == String::class) {
                    if (param.name!!.contains("id") || param.name!!.contains("Id")) {
                        mapData[param] = sequence.nextId().toString()
                    } else {
                        mapData[param] = param.name + "_" + sequence.nextId().toString().substring(6, 9)
                    }
                }
                if (param.type.classifier == Date::class) {
                    mapData[param] = tenantDateTime.now().toDate()
                }
                if (param.type.classifier == BigDecimal::class) {
                    mapData[param] = BigDecimal(0.1)
                }
                if (param.type.classifier == Boolean::class) {
                    mapData[param] = false
                }
                if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                    mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
                }
            }
        }

        return constructor.callBy(mapData)
    }
}