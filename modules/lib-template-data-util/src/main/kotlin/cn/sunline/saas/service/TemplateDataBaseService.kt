package cn.sunline.saas.service

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses


abstract class TemplateDataBaseService(defaultMap: Map<String, Any>? = null) {

    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    abstract fun <T : Any> deepCustomAssignment(
        type: KClass<T>,
        map: Map<String, Any>? = mutableMapOf()
    ): T

    protected fun <T : Any> deepBaseAssignment(
        type: KClass<T>,
        map: Map<String, Any>? = mutableMapOf()
    ): T {
        return type.primaryConstructor!!.let { primaryConstructor ->
            primaryConstructor.parameters.associateWith { param ->

                var any: Any? = null
                if (param.type.classifier == Long::class) {
                    any = sequence.nextId()
                }
                if (param.type.classifier == String::class) {
                    any = if (param.name!!.contains("id",true)) {
                        sequence.nextId().toString().substring(6, 9)
                    } else if (param.name!!.contains("date", true)) {
                        tenantDateTime.getYearMonthDay(tenantDateTime.now())
                    } else {
                        "${param.name?.substring(0, 2)}_${sequence.nextId().toString().substring(6, 9)}"
                    }
                }
                if (param.type.classifier == Date::class) {
                    any = tenantDateTime.now().toDate()
                }
                if (param.type.classifier == DateTime::class) {
                    any = tenantDateTime.now()
                }
                if (param.type.classifier == Boolean::class) {
                    any = false
                }
                if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                    any = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
                }
                if (param.type.classifier == List::class) {
                    any = mutableListOf<Any>()
                    param.type.arguments.forEach {
                        if ((it.type?.classifier as? KClass<*>)?.isData == true) {
                            any as MutableList<Any> += deepBaseAssignment((it.type?.classifier as? KClass<*>)!!, map)
                        }
                    }
                }
                if (param.type.classifier == Map::class) {
                    any = mutableMapOf<Any, Any>()
                }
                if ((param.type.classifier as? KClass<*>)?.isData == true) {
                    any = deepBaseAssignment((param.type.classifier as? KClass<*>)!!, map)
                }

                val parentTypeName = (primaryConstructor.returnType.classifier as? KClass<*>)!!
                val paramTypeName = param.name!!
                any = getValue(
                    parentTypeName,
                    paramTypeName,
                    any,
                    map
                )
                any
            }
        }.let {
            type.primaryConstructor!!.callBy(it)
        }
    }


    private fun getValue(
        currentTypeName: KClass<*>,
        fieldName: String,
        defaultValue: Any?,
        defaultMap: Map<String, Any>? = mutableMapOf()
    ): Any? {
        if (!defaultMap.isNullOrEmpty()) {

            if (defaultMap.containsKey(currentTypeName.simpleName!!.lowercase())) {
                val get = defaultMap[currentTypeName.simpleName!!.lowercase()] as Map<*, *>
                return get[fieldName] ?: defaultValue
            }
            if (defaultMap.containsKey(fieldName)) {
                return defaultMap[fieldName] ?: defaultValue
            }
        }
        return defaultValue
    }
}


//data class MyT(
//    val a: Long,
//    val myt2: MyT2,
//    val mtl: MutableList<MyT2>,
//    val map: MutableMap<String, String>
//
//)
//
//data class MyT2(
//    val name: String,
//    val myT3: MyT3
//)
//
//data class MyT3(
//    val name: String,
//)
//data class MyTest(
//    val mm: MutableList<Test> = mutableListOf<Test>(),
//)
//
//data class Test(
//    val a: Long,
//    val b: Long,
//    val test2: Test2
//)
//
//data class Test2(
//    val c: Long,
//    val b: Long
//)
//
//class TemplateDataService : TemplateDataBaseService() {
//    override fun <T : Any> deepCustomAssignment(type: KClass<T>, map: Map<String, Any>?): T {
//        return deepBaseAssignment(type, map)
//    }
//
//}
//
//fun main(args: Array<String>) {
////    val templateData = getTemplateData(TestClass::class, false)
////    val i =0
//    // MutableList<MyT2>
//    val testMap = mutableMapOf<String, Any>()
//    testMap["c"] = 4444L
//
//    val map = mutableMapOf<String, Any>()
//    map["a"] = 3333333L
//    map["test2"] = testMap
//    val deepAssignment = TemplateDataService().deepCustomAssignment(MyTest::class, map)
//
//
//    println(deepAssignment)
//}
