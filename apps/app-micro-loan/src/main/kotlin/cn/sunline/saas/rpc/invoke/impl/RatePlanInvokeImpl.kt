package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUUID
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.rpc.invoke.RatePlanInvoke
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeRatePlanRates
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service

@Service
class RatePlanInvokeImpl: RatePlanInvoke {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun getRatePlanByType(type: RatePlanType): DTOInvokeRatePlanRates {
        val ratePlanResponse = RPCService.get<DTOResponseSuccess<DTOInvokeRatePlanRates>>(
            serviceName = APP_LOAN_MANAGEMENT,
            methodName = "RatePlan/invokeAll",
            queryParams = mapOf(
                "type" to type.toString()
            ),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant()
        )
        // TODO Throw Exception
        return objectMapper.convertValue(ratePlanResponse?.data!!)
    }

    override fun getRatePlanByRatePlanId(ratePlanId: Long): DTOInvokeRatePlanRates {
        val ratePlanResponse = RPCService.get<DTOResponseSuccess<DTOInvokeRatePlanRates>>(
            serviceName = APP_LOAN_MANAGEMENT,
            methodName = "RatePlan/$ratePlanId",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()
        )
        // TODO Throw Exception
        return ratePlanResponse?.data!!
    }
}