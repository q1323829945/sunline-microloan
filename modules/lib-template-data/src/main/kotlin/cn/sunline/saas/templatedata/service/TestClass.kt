package cn.sunline.saas.templatedata.service

import org.joda.time.DateTime
import org.springframework.boot.runApplication
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses
import kotlin.reflect.javaType
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.reflect


/**
 * @tittle :
 * @description :
 * @author : xujm
 * @date : 2022/10/31 14:56
 */

data class MyT(
    val a:Long,
    val myt2:MyT2,
    val mtl:MutableList<MyT2>,
    val map:MutableMap<String,String>

)

data class MyT2(
    val name:String,
    val myT3: MyT3
)

data class MyT3(
    val name:String,
)




fun <T:Any>KClass<T>.deepAssignment(map:Map<String,KClass<*>> = mutableMapOf()): T{
    return this.primaryConstructor!!.let { primaryConstructor ->
        primaryConstructor.parameters.associateWith { param ->
            var any:Any? = null

            if(param.type.classifier == Long::class){
                any = 111
            }
            if(param.type.classifier == String::class){
                any = "888811L"
            }
            if(param.type.classifier == Date::class){
                any = DateTime.now().toDate()
            }
            if(param.type.classifier == Boolean::class){
                any = false
            }

            if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                any = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
            }

            if(param.type.classifier == List::class){
                map[param.name]?.run {
                    any = mutableListOf(this.deepAssignment())
                }
            }

            if(param.type.classifier == Map::class){
                any = mutableMapOf<Any,Any>()
            }

            if((param.type.classifier as? KClass<*>)?.isData == true){
                any = (param.type.classifier as? KClass<*>)!!.deepAssignment()
            }
            any
        }
    }.let {
        this.primaryConstructor!!.callBy(it)
    }

}



fun main(args: Array<String>) {
//    val templateData = getTemplateData(TestClass::class, false)
//    val i =0

    val a = MyT::class.deepAssignment()

    println(a)
}
//
//fun <T: Any> getTemplateData(type: KClass<T>, overrideDefaults: Boolean): T {
//    val constructor = type.primaryConstructor!!
//    val parameters = constructor.parameters
////        if (!parameters.all { param -> (param.type.classifier == String::class || param.isOptional)||
////                    (param.type.classifier == Long ::class || param.isOptional)}){
////            error("Class $type primary constructor has required non-String parameters.")
////        }
//
//    val mapData =  mutableMapOf<KParameter, Any?>()
//    constructor.parameters.forEach { param ->
//        if(param.type.classifier == Long::class){
//            mapData[param] = 111
//        }
//        if(param.type.classifier == String::class){
//            mapData[param] = "888811L"
//        }

//        if(param.type.classifier == Date::class){
//            mapData[param] = DateTime.now().toDate()
//        }
//        if(param.type.classifier == Boolean::class){
//            mapData[param] = false
//        }
//    }
//
//    return constructor.callBy(mapData)
//}