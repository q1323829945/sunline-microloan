package cn.sunline.saas.templatedata.service

import org.joda.time.DateTime
import org.springframework.boot.runApplication
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor


/**
 * @tittle :
 * @description :
 * @author : xujm
 * @date : 2022/10/31 14:56
 */
class Test {
}
fun main(args: Array<String>) {
    val templateData = getTemplateData(TestClass::class, false)
    val i =0
}

fun <T: Any> getTemplateData(type: KClass<T>, overrideDefaults: Boolean): T {
    val constructor = type.primaryConstructor!!
    val parameters = constructor.parameters
//        if (!parameters.all { param -> (param.type.classifier == String::class || param.isOptional)||
//                    (param.type.classifier == Long ::class || param.isOptional)}){
//            error("Class $type primary constructor has required non-String parameters.")
//        }

    val mapData =  mutableMapOf<KParameter, Any?>()
    constructor.parameters.forEach { param ->
        if(param.type.classifier == Long::class){
            mapData[param] = 111
        }
        if(param.type.classifier == String::class){
            mapData[param] = "888811L"
        }
        if(param.type.classifier == Date::class){
            mapData[param] = DateTime.now().toDate()
        }
        if(param.type.classifier == Boolean::class){
            mapData[param] = false
        }
    }

    return constructor.callBy(mapData)
}