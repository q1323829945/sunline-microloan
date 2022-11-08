//package cn.sunline.saas.templatedata.service.impl
//
//import cn.sunline.saas.channel.interest.model.RatePlanType
//import cn.sunline.saas.channel.interest.service.RatePlanService
//import cn.sunline.saas.global.constant.LoanTermType
//import cn.sunline.saas.multi_tenant.util.TenantDateTime
//import cn.sunline.saas.seq.Sequence
//import cn.sunline.saas.templatedata.exception.TemplateDataBusinessException
//import cn.sunline.saas.templatedata.service.TemplateDataService
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Service
//import kotlin.reflect.KClass
//import kotlin.reflect.KParameter
//import kotlin.reflect.full.primaryConstructor
//import kotlin.reflect.full.superclasses
//
//
//@Service
//class ChannelInterestRateTemplateDataServiceImpl : TemplateDataService() {
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
//
//    fun <T : Any> getTemplateData(type: KClass<T>, ratePlanId: String, overrideDefaults: Boolean): T {
//        val ratePlan =
//            ratePlanService.getOne(ratePlanId.toLong()) ?: throw TemplateDataBusinessException("Invalid RatePlan")
//        val map = mutableMapOf<String, Any>()
//        when (ratePlan.type) {
//            RatePlanType.STANDARD,
//            RatePlanType.CUSTOMER -> {
//                map["period"] = LoanTermType.ONE_MONTH
//                map["ratePlanId"] = ratePlanId
//                map["rate"] = "0.2"
//            }
//        }
//        return getTemplateData(type, map, overrideDefaults)
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
//
//            if(defaultMapData != null && defaultMapData.containsKey(param.name!!)){
//                mapData[param] = defaultMapData[param.name!!]
//            }
//            if(!param.isOptional){
//                if (param.type.classifier == Long::class) {
//                    mapData[param] = sequence.nextId()
//                }
//                if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
//                    mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
//                }
//            }else{
//                mapData[param] = null
//            }
//        }
//
//        return constructor.callBy(mapData)
//    }
//}