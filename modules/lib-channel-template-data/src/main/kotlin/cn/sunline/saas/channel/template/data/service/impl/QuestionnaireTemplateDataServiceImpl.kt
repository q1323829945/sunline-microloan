package cn.sunline.saas.channel.template.data.service.impl//package cn.sunline.saas.templatedata.service.impl

import cn.sunline.saas.channel.template.data.service.TemplateDataService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses


@Service
class QuestionnaireTemplateDataServiceImpl : TemplateDataService() {

    @Autowired
    private lateinit var sequence: Sequence

    override fun <T : Any> getTemplateData(
        type: KClass<T>,
        defaultMapData: Map<String, Any>?,
        overrideDefaults: Boolean
    ): T {
        val constructor = type.primaryConstructor!!
        val mapData = mutableMapOf<KParameter, Any?>()
        constructor.parameters.forEach { param ->
            if (defaultMapData != null && defaultMapData.containsKey(param.name!!)) {
                mapData[param] = defaultMapData[param.name!!]
            }
            if (!param.isOptional) {
                if (param.type.classifier == String::class) {
                    mapData[param] = "${param.name?.substring(0, 2)}_${sequence.nextId().toString().substring(6, 9)}"
                }
                if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                    mapData[param] = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
                }
            }
        }

        return constructor.callBy(mapData)
    }
}