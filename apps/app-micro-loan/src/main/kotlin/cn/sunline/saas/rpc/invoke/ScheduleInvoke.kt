package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.consumer_loan.service.dto.DTORatePlanView
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.response.DTOResponseSuccess


interface ScheduleInvoke {

    fun getRatePlan(ratePlanId: Long): DTOResponseSuccess<DTORatePlanView>?

    fun getRatePlanByRatePlanType(ratePlanType: RatePlanType): DTOResponseSuccess<DTORatePlanView>?

}