package cn.sunline.saas.schedule.invoke.impl

import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.schedule.dto.DTORatePlanView
import cn.sunline.saas.schedule.invoke.ScheduleInvoke
import io.dapr.client.domain.HttpExtension
import org.springframework.stereotype.Component

@Component
class ScheduleInvokeImpl: ScheduleInvoke {

    private val applId = "app-loan-management"

    override fun getRatePlan(ratePlanId: Long): DTOResponseSuccess<DTORatePlanView>? {
        return DaprHelper.invoke(
            applId,
            "RatePlan/${ratePlanId}",
            null,
            HttpExtension.GET,
            DTOResponseSuccess<DTORatePlanView>()::class.java
        )
    }

    override fun getRatePlanByRatePlanType(ratePlanType: RatePlanType): DTOResponseSuccess<DTORatePlanView>? {
        return DaprHelper.invoke(
            applId,
            "RatePlan/all?type=${ratePlanType}",
            null,
            HttpExtension.GET,
            DTOResponseSuccess<DTORatePlanView>()::class.java
        )
    }
}