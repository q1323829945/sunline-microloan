package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.rpc.invoke.dto.DTOBusinessUnit
import cn.sunline.saas.rpc.invoke.dto.DTOInterestRate

interface ProductInvoke {
    fun getInterestRate(ratePlanId:Long):List<DTOInterestRate>
    fun getBusinessUnit(id:Long):DTOBusinessUnit

}