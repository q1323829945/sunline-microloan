package cn.sunline.saas.pdpa.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.global.constant.meta.Header
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.global.util.getUserId
import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.pdpa.invoke.PdpaInvoke
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.stereotype.Service

@Service
class PdpaInvokeImpl: PdpaInvoke {

    private val applId = "app-loan-management"

    override fun getPDPAInformation(countryCode: String): DTOResponseSuccess<PDPAInformation>? {
        return RPCService.get<DTOResponseSuccess<PDPAInformation>>(
            serviceName = applId,
            methodName = "pdpa/$countryCode/retrieve",
            queryParams = mapOf(),
            headerParams = mapOf(
                Header.TENANT_AUTHORIZATION.key to ContextUtil.getTenant().toString(),
                Header.USER_AUTHORIZATION.key to ContextUtil.getUserId()
            ),
            tenant = ContextUtil.getTenant().toString()
        )
    }
}