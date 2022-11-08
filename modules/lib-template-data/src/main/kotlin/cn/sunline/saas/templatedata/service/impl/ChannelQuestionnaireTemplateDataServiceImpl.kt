//package cn.sunline.saas.templatedata.service.impl
//
//import cn.sunline.saas.global.constant.LoanTermType
//import cn.sunline.saas.interest.model.RatePlanType
//import cn.sunline.saas.interest.service.RatePlanService
//import cn.sunline.saas.multi_tenant.util.TenantDateTime
//import cn.sunline.saas.seq.Sequence
//import cn.sunline.saas.templatedata.exception.TemplateDataBusinessException
//import cn.sunline.saas.templatedata.service.TemplateDataService
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Service
//import java.math.BigDecimal
//import java.util.*
//import kotlin.reflect.KClass
//import kotlin.reflect.KParameter
//import kotlin.reflect.full.primaryConstructor
//import kotlin.reflect.full.superclasses
//
//
//@Service
//class ChannelQuestionnaireTemplateDataServiceImpl : TemplateDataService() {
//
//    @Autowired
//    private lateinit var sequence: Sequence
//
//    override fun <T : Any> getTemplateData(
//        type: KClass<T>,
//        defaultMapData: Map<String, Any>?,
//        overrideDefaults: Boolean
//    ): T {
//        val constructor = type.primaryConstructor!!
//        val mapData = mutableMapOf<KParameter, Any?>()
//        constructor.parameters.forEach { param ->
//            if (defaultMapData != null && defaultMapData.containsKey(param.name!!)) {
//                mapData[param] = defaultMapData[param.name!!]
//            }
//            if (!param.isOptional) {
//                if (param.type.classifier == String::class) {
//                    mapData[param] = "888811L"
//                }
//                if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
//                    mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
//                }
//            }
//        }
//
//        return constructor.callBy(mapData)
//    }
//}