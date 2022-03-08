package cn.sunline.saas.rule.aviator.function

import com.googlecode.aviator.runtime.function.AbstractFunction
import com.googlecode.aviator.runtime.function.FunctionUtils
import com.googlecode.aviator.runtime.type.AviatorDouble
import com.googlecode.aviator.runtime.type.AviatorObject


class MultiplyFunction : AbstractFunction() {

    override fun call(env:Map<String,Any>,arg1:AviatorObject,arg2:AviatorObject): AviatorObject {
        val number1 = FunctionUtils.getNumberValue(arg1,env).toDouble()
        val number2 = FunctionUtils.getNumberValue(arg2,env).toDouble()
        return AviatorDouble(number1 * number2)
    }

    override fun getName(): String {
        return "multiply"
    }
}