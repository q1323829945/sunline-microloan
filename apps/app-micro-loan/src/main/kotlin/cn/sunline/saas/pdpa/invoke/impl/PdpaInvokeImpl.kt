package cn.sunline.saas.pdpa.invoke.impl

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.pdpa.invoke.PdpaInvoke
import io.dapr.client.domain.HttpExtension
import org.springframework.stereotype.Service

@Service
class PdpaInvokeImpl: PdpaInvoke {

    private val applId = "app-loan-management"

    override fun getPDPAInformation(countryCode: String): PDPAInformation? {
        return DaprHelper.invoke(
            applId,
            "pdpa/{countryCode}/retrieve",
            "{\"countryCode\":\"$countryCode\"}",
            HttpExtension.GET,
            PDPAInformation::class.java
        )
    }
}