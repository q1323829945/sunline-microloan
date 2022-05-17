package cn.sunline.saas.product.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.product.invoke.ProductInvoke
import cn.sunline.saas.loan.product.model.dto.DTOLoanProduct
import cn.sunline.saas.loan.product.model.dto.DTOLoanProductView
import cn.sunline.saas.response.DTOResponseSuccess
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Service

@Service
class ProductInvokeImpl: ProductInvoke {

    private val applId = "app-loan-management"

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    override fun getProductInfoByProductId(productId: Long): DTOResponseSuccess<DTOLoanProductView>? {
        return RPCService.get<DTOResponseSuccess<DTOLoanProductView>>(
            serviceName = applId,
            methodName = "LoanProduct/$productId",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()
        )
    }

    override fun getProductListByIdentificationCode(identificationCode: String): DTOResponseSuccess<MutableList<DTOLoanProduct>>? {
        return RPCService.get<DTOResponseSuccess<MutableList<DTOLoanProduct>>>(
            serviceName = applId,
            methodName = "LoanProduct/$identificationCode/retrieve",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()
        )
    }
}