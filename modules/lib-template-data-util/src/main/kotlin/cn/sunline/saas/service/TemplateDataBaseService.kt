package cn.sunline.saas.service

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.seq.Sequence
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.superclasses

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
//

abstract class TemplateDataBaseService(defaultMap: Map<String, Any>? = null) {

    @Autowired
    private lateinit var sequence: Sequence

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime

    abstract fun <T : Any> deepCustomAssignment(
        type: KClass<T>,
        map: Map<String, KClass<*>>? = mutableMapOf()
    ): T

    protected fun <T : Any> deepBaseAssignment(
        type: KClass<T>,
        map: Map<String, KClass<*>>? = mutableMapOf()
    ): T {
        return type.primaryConstructor!!.let { primaryConstructor ->
            primaryConstructor.parameters.associateWith { param ->
                var any: Any? = null

                if (param.type.classifier == Long::class) {
                    any = 111
                }
                if (param.type.classifier == String::class) {
                    if (param.name == "id" || param.name!!.contains("Id") || param.name!!.contains("id")) {
                        any = sequence.nextId().toString().substring(6, 9)
                    } else {
                        any = "${param.name?.substring(0, 2)}_${sequence.nextId().toString().substring(6, 9)}"
                    }
                }
                if (param.type.classifier == Date::class) {
                    any = DateTime.now().toDate()
                }
                if (param.type.classifier == Boolean::class) {
                    any = false
                }

                if ((param.type.classifier as KClass<*>).superclasses.first() == Enum::class) {
                    any = (Class.forName((param.type as Any).toString()).enumConstants as Array<*>).first()
                }
                if (param.type.classifier == List::class) {
//                    map[param.name]?.run {
//                        any = mutableListOf(deepAssignment(this))
//                    }
                    //(param.type.arguments.first().type.classifier as? KClass<*>)?.isData
                    //any = param.type
                    any = mutableListOf<Any>()
                    param.type.arguments.forEach {
                        if ((it.type?.classifier as? KClass<*>)?.isData == true) {
                            any as MutableList<Any> += deepBaseAssignment((it.type?.classifier as? KClass<*>)!!)
                        }
                    }
                }
                if (param.type.classifier == Map::class) {
                    any = mutableMapOf<Any, Any>()
                }

                if ((param.type.classifier as? KClass<*>)?.isData == true) {
                    any = deepBaseAssignment((param.type.classifier as? KClass<*>)!!)
                }
                any
            }
        }.let {
            type.primaryConstructor!!.callBy(it)
        }
    }
}

//data class MyTest(
//    val mm: MutableList<Test> = mutableListOf<Test>(),
//)
//
//data class Test(
//    val a: Long,
//)

//fun main(args: Array<String>) {
////    val templateData = getTemplateData(TestClass::class, false)
////    val i =0
//    // MutableList<MyT2>
//    val deepAssignment = TemplateDataService().deepAssignment(MyTest::class)
//
//
//    println(deepAssignment)
//}
////