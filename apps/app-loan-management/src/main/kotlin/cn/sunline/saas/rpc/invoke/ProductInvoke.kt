package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.rpc.invoke.dto.DTOBusinessUnit
import cn.sunline.saas.rpc.invoke.dto.DTOInterestRate
import cn.sunline.saas.rpc.invoke.dto.DTORatePlanWithInterestRates

interface ProductInvoke {
    fun getInterestRate(ratePlanId: Long): List<DTOInterestRate>

    fun getBusinessUnit(id: Long): DTOBusinessUnit

    fun getRatePlanWithInterestRate(ratePlanId: Long): DTORatePlanWithInterestRates
}