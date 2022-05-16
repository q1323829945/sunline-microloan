package cn.sunline.saas.pdpa.invoke.impl

import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.pdpa.invoke.PdpaInvoke
import cn.sunline.saas.response.DTOResponseSuccess
import io.dapr.client.domain.HttpExtension
import org.springframework.stereotype.Service

@Service
class PdpaInvokeImpl: PdpaInvoke {

    private val applId = "app-loan-management"

    override fun getPDPAInformation(countryCode: String): DTOResponseSuccess<PDPAInformation>? {
        return DaprHelper.invoke(
            applId,
            "pdpa/$countryCode/retrieve",
            null,
            HttpExtension.GET,
            DTOResponseSuccess<PDPAInformation>()::class.java
        )
    }
}