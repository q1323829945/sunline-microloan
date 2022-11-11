package cn.sunline.saas.channel.template.data.service

import kotlin.reflect.KClass


abstract class TemplateDataService {

    abstract fun <T : Any> getTemplateData(
        type: KClass<T>,
        defaultMapData: Map<String, Any>?,
        overrideDefaults: Boolean
    ): T
}