package cn.sunline.saas.rpc.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.dapr_wrapper.invoke.response.RPCResponse
import cn.sunline.saas.global.constant.APP_LOAN_MANAGEMENT
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.rpc.invoke.PdpaInvoke
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.stereotype.Service

@Service
class PdpaInvokeImpl: PdpaInvoke {
    override fun getPDPAInformation(countryCode: String): RPCResponse<PDPAInformation>? {
        return RPCService.get<RPCResponse<PDPAInformation>>(
            serviceName = APP_LOAN_MANAGEMENT,
            methodName = "pdpa/$countryCode/retrieve",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant()
        )
    }
}