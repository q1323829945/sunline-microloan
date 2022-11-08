//package cn.sunline.saas.templatedata.service.impl
//
//import cn.sunline.saas.channel.interest.model.RatePlanType
//import cn.sunline.saas.channel.interest.service.RatePlanService
//import cn.sunline.saas.channel.product.service.QuestionnaireService
//import cn.sunline.saas.multi_tenant.util.TenantDateTime
//import cn.sunline.saas.seq.Sequence
//import cn.sunline.saas.templatedata.exception.TemplateDataBusinessException
//import cn.sunline.saas.templatedata.service.TemplateDataService
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.data.domain.Pageable
//import org.springframework.stereotype.Service
//import kotlin.reflect.KClass
//import kotlin.reflect.KParameter
//import kotlin.reflect.full.primaryConstructor
//import kotlin.reflect.full.superclasses
//
//
//@Service
//class ChannelLoanProductTemplateServiceImpl : TemplateDataService() {
//
//
//    @Autowired
//    private lateinit var sequence: Sequence
//
//    @Autowired
//    private lateinit var tenantDateTime: TenantDateTime
//
//    @Autowired
//    private lateinit var ratePlanService: RatePlanService
//
//    @Autowired
//    private lateinit var questionnaireService: QuestionnaireService
//
//
//    private fun getQuestionnaires(): MutableList<String>? {
//        val content = questionnaireService.paged(Pageable.unpaged()).content
//        if (content.isEmpty()) {
//            return null
//            //throw TemplateDataBusinessException("questionnaires is empty")
//        }
//        val list = mutableListOf<String>()
//        content.forEach {
//            list += it.id
//        }
//        return list
//    }
//
//    private fun getRatePlanId(): String {
//        return ratePlanService.getByRatePlanType(
//            RatePlanType.CUSTOMER,
//            Pageable.unpaged()
//        ).content.first().id.toString()
//    }
//
//    override fun <T : Any> getTemplateData(
//        type: KClass<T>,
//        defaultMapData: Map<String, Any>?,
//        overrideDefaults: Boolean
//    ): T {
//        val constructor = type.primaryConstructor!!
//        val mapData = mutableMapOf<KParameter, Any?>()
//        constructor.parameters.forEach { param ->
//            if (param.type.classifier == String::class) {
//                mapData[param] = "product_" + sequence.nextId().toString().substring(6, 9)
//            }
//            if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
//                mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
//            }
//            if (param.name == "ratePlanId") {
//                mapData[param] = getRatePlanId()
//            }
//            if (param.name!! == "questionnaires") {
//                mapData[param] = getQuestionnaires()
//            }
//        }
//
//        return constructor.callBy(mapData)
//    }
//}