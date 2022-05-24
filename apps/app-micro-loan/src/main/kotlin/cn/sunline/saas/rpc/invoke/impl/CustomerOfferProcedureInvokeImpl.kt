package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.rpc.invoke.CustomerOfferProcedureInvoke
import cn.sunline.saas.rpc.invoke.dto.DTOProductUploadConfig
import org.springframework.stereotype.Component

@Component
class CustomerOfferProcedureInvokeImpl: CustomerOfferProcedureInvoke {

    private val applId = "app-loan-management"

    override fun getProductUploadConfig(productId: Long): List<DTOProductUploadConfig> {
        return RPCService.get(
            applId,
            "LoanProduct/uploadConfig/$productId",
            mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()
        )!!
    }
}