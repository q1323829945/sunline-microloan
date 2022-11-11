package cn.sunline.saas.tempalte.data.service.impl

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import cn.sunline.saas.service.TemplateDataBaseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.reflect.KClass


@Service
class CommonTemplateDataServiceImpl: TemplateDataBaseService() {

    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime
    override fun <T : Any> deepCustomAssignment(type: KClass<T>, map: Map<String, KClass<*>>?): T {
        return deepBaseAssignment(type,map)
    }

//    fun <T : Any> dee(
//        type: KClass<T>,
//        map: Map<String, KClass<*>>
//    ): T {
//        return super.deepAssignment(type,map)
//    }
//    override fun <T: Any> getTemplateData(type: KClass<T>,defaultMapData: Map<String, Any>?, overrideDefaults: Boolean): T {
//        val constructor = type.primaryConstructor!!
//        val parameters = constructor.parameters
////        if (!parameters.all { param -> (param.type.classifier == String::class || param.isOptional)||
////                    (param.type.classifier == Long ::class || param.isOptional)}){
////            error("Class $type primary constructor has required non-String parameters.")
////        }
//
//        val mapData =  mutableMapOf<KParameter, Any?>()
//        constructor.parameters.forEach { param ->
//            if(param.type.classifier == Long::class){
//                mapData[param] = sequence.nextId()
//            }
//            if(param.type.classifier == String::class){
//                if(param.name!!.contains("id")){
//                    mapData[param] = sequence.nextId().toString()
//                }else {
//                    mapData[param] = param.name + "_" + sequence.nextId().toString().substring(6, 9)
//                }
//            }
//            if(param.type.classifier == Date::class){
//                mapData[param] = tenantDateTime.now().toDate()
//            }
//            if(param.type.classifier == Boolean::class){
//                mapData[param] = false
//            }
//            if((param.type.classifier as KClass<*>).superclasses.first() == Enum::class){
//                mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
//            }
//        }
//
//        return constructor.callBy(mapData)
//
////        val valuesByParameter = parameters.filter {
////            (it.type.classifier == String::class && (!it.isOptional || overrideDefaults) )||
////                    (it.type.classifier == Long::class || overrideDefaults) }
////            .associateWith(KParameter::name)
//        //  return constructor.callBy(valuesByParameter)
//    }
}