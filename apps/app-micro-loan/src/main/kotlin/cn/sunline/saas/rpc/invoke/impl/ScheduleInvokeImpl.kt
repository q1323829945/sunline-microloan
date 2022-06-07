package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.consumer_loan.service.dto.DTORatePlanView
import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.rpc.invoke.ScheduleInvoke
import org.springframework.stereotype.Component

@Component
class ScheduleInvokeImpl: ScheduleInvoke {

    private val applId = "app-loan-management"

    override fun getRatePlan(ratePlanId: Long): DTOResponseSuccess<DTORatePlanView>? {
        return RPCService.get<DTOResponseSuccess<DTORatePlanView>>(
            serviceName = applId,
            methodName = "RatePlan/${ratePlanId}",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()
        )
    }

    override fun getRatePlanByRatePlanType(ratePlanType: RatePlanType): DTOResponseSuccess<DTORatePlanView>? {

        return RPCService.get<DTOResponseSuccess<DTORatePlanView>>(
            serviceName = applId,
            methodName = "RatePlan/all",
            queryParams = mapOf("type" to "$ratePlanType"),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()
        )
    }
}