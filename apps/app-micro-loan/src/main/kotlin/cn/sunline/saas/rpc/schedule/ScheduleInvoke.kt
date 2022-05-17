package cn.sunline.saas.rpc.schedule

import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.schedule.dto.DTORatePlanView


interface ScheduleInvoke {

    fun getRatePlan(ratePlanId: Long): DTOResponseSuccess<DTORatePlanView>?

    fun getRatePlanByRatePlanType(ratePlanType: RatePlanType): DTOResponseSuccess<DTORatePlanView>?

}