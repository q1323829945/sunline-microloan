package cn.sunline.saas.underwriting.invoke.impl

import cn.sunline.saas.dapr_wrapper.DaprHelper
import cn.sunline.saas.partner.integrated.model.dto.DTOPartnerIntegrated
import cn.sunline.saas.underwriting.invoke.UnderwritingInvoke
import io.dapr.client.domain.HttpExtension
import org.springframework.stereotype.Component

/**
 * @title: UnderwritingInvokeImpl
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 11:07
 */
@Component
class UnderwritingInvokeImpl : UnderwritingInvoke {

    private val applId = "app-loan-management"

    override fun getPartnerIntegrated(): DTOPartnerIntegrated? {
        return DaprHelper.invoke(
            applId,
            "PartnerIntegrated/Retrieve",
            null,
            HttpExtension.GET,
            DTOPartnerIntegrated::class.java
        )
    }
}