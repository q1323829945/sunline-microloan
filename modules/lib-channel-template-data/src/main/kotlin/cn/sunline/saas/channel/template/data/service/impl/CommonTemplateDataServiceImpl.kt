package cn.sunline.saas.channel.template.data.service.impl

import cn.sunline.saas.channel.template.data.service.TemplateDataService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses


@Service
class CommonTemplateDataServiceImpl : TemplateDataService() {

    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime


    override fun <T : Any> getTemplateData(
        type: KClass<T>,
        defaultMapData: Map<String, Any>?,
        overrideDefaults: Boolean
    ): T {
        val constructor = type.primaryConstructor!!
        val parameters = constructor.parameters
//        if (!parameters.all { param -> (param.type.classifier == String::class || param.isOptional)||
//                    (param.type.classifier == Long ::class || param.isOptional)}){
//            error("Class $type primary constructor has required non-String parameters.")
//        }

        val mapData = mutableMapOf<KParameter, Any?>()
        constructor.parameters.forEach { param ->
            if (param.type.classifier == Long::class) {
                mapData[param] = sequence.nextId()
            }
            if (param.type.classifier == String::class) {
                if (param.name!!.contains("id")) {
                    mapData[param] = sequence.nextId().toString()
                } else {
                    mapData[param] = "${param.name?.substring(0, 2)}_${sequence.nextId().toString().substring(6, 9)}"
                }
            }
            if (param.type.classifier == Date::class) {
                mapData[param] = tenantDateTime.now().toDate()
            }
            if (param.type.classifier == Boolean::class) {
                mapData[param] = false
            }
            if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
            }
        }

        return constructor.callBy(mapData)

//        val valuesByParameter = parameters.filter {
//            (it.type.classifier == String::class && (!it.isOptional || overrideDefaults) )||
//                    (it.type.classifier == Long::class || overrideDefaults) }
//            .associateWith(KParameter::name)
        //  return constructor.callBy(valuesByParameter)
    }
}