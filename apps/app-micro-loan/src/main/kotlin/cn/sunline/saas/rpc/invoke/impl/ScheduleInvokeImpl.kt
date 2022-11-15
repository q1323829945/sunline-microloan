package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.consumer_loan.service.dto.DTORatePlanView
import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.dapr_wrapper.constant.APP_LOAN_MANAGEMENT
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUUID
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.interest.model.RatePlanType
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.rpc.exception.RatePlanNotFoundException
import cn.sunline.saas.rpc.invoke.ScheduleInvoke
import cn.sunline.saas.rpc.invoke.dto.DTOInvokeRatePlanRates
import com.fasterxml.jackson.module.kotlin.convertValue
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class ScheduleInvokeImpl: ScheduleInvoke {
    private val logger = KotlinLogging.logger {  }

    override fun getRatePlan(ratePlanId: Long): DTOResponseSuccess<DTORatePlanView>? {
        return try {
            RPCService.get<DTOResponseSuccess<DTORatePlanView>>(
                serviceName = APP_LOAN_MANAGEMENT,
                methodName = "RatePlan/${ratePlanId}",
                queryParams = mapOf(),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID().toString(),
                ),
                tenant = ContextUtil.getTenant().toString()
            )
        } catch (e:Exception){
            logger.error { e.localizedMessage }
            throw RatePlanNotFoundException("Invalid rate plan!!")
        }
    }

    override fun getRatePlanByRatePlanType(ratePlanType: RatePlanType): DTOResponseSuccess<DTORatePlanView>? {

        return try {
            RPCService.get<DTOResponseSuccess<DTORatePlanView>>(
                serviceName = APP_LOAN_MANAGEMENT,
                methodName = "RatePlan/all",
                queryParams = mapOf("type" to "$ratePlanType"),
                headerParams = mapOf(
                    Header.TENANT_AUTHORIZATION.key to ContextUtil.getUUID().toString(),
                ),
                tenant = ContextUtil.getTenant().toString()
            )
        } catch (e:Exception){
            logger.error { e.localizedMessage }
            throw RatePlanNotFoundException("Invalid rate plan!!")
        }
    }
}