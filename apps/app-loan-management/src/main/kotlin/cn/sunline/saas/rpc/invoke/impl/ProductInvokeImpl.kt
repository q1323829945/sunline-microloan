package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.interest.exception.RatePlanNotFoundException
import cn.sunline.saas.party.organisation.exception.BusinessUnitNotFoundException
import cn.sunline.saas.rpc.invoke.ProductInvoke
import cn.sunline.saas.rpc.invoke.dto.DTOBusinessUnit
import cn.sunline.saas.rpc.invoke.dto.DTOInterestRate
import org.springframework.stereotype.Component

@Component
class ProductInvokeImpl: ProductInvoke {
    private val applId = "app-loan-management"

    override fun getInterestRate(ratePlanId: Long): List<DTOInterestRate> {

        return RPCService.get<List<DTOInterestRate>>(
            serviceName = applId,
            methodName = "InterestRate/all",
            queryParams = mapOf("ratePlanId" to ratePlanId.toString()),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()
        )?:throw RatePlanNotFoundException("Invalid rate plan")
    }

    override fun getBusinessUnit(id: Long): DTOBusinessUnit {
        return RPCService.get(
            serviceName = applId,
            methodName = "BusinessUnit/$id",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()
        )?:throw BusinessUnitNotFoundException("Invalid businessUnit")
    }
}