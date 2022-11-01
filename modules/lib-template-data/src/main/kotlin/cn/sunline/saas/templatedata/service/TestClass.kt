package cn.sunline.saas.templatedata.service

import org.springframework.boot.runApplication
import java.util.Date
import kotlin.reflect.KParameter
import kotlin.reflect.KType


/**
 * @tittle :
 * @description :
 * @author : xujm
 * @date : 2022/10/31 11:08
 */
class TestClass(
    val id: String,
    val name: Boolean,
    val age: Long = 0L,
    val date: Date
)


