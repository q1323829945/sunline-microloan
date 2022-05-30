package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeRatePlanRates

interface RatePlanInvoke {

    fun getRatePlanByType(type: RatePlanType): DTOInvokeRatePlanRates

    fun getRatePlanByRatePlanId(ratePlanId: Long): DTOInvokeRatePlanRates
}