package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.dapr_wrapper.invoke.response.RPCResponse
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.rpc.invoke.CustomerOfferInvoke
import cn.sunline.saas.rpc.invoke.dto.DTOUnderwriting
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component

@Component
class CustomerOfferInvokeImpl: CustomerOfferInvoke {

    private val applId = "app-loan-management"
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun getProduct(productId: Long): DTOLoanProductView {
        val product =  RPCService.get<RPCResponse<DTOLoanProductView>>(
            serviceName = applId,
            methodName = "LoanProduct/$productId",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant()
        )

        return objectMapper.convertValue(product!!.data)
    }

    override fun getUnderwriting(applicationId: Long): DTOUnderwriting? {
        val underwriting = RPCService.get<RPCResponse<DTOUnderwriting>>(
            serviceName = applId,
            methodName = "UnderwritingManagement/$applicationId",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant()
        )

        return underwriting?.run { objectMapper.convertValue(underwriting.data) }
    }
}