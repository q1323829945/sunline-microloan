package cn.sunline.saas.underwriting.invoke.impl

import cn.sunline.saas.dapr_wrapper.invoke.RPCService
import cn.sunline.saas.partner.integrated.model.dto.DTOPartnerIntegrated
import cn.sunline.saas.underwriting.invoke.UnderwritingInvoke
import cn.sunline.saas.underwriting.invoke.impl.dto.PartnerIntegratedRPCRequest
import org.springframework.stereotype.Component

/**
 * @title: UnderwritingInvokeImpl
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/11 11:07
 */
@Component
class UnderwritingInvokeImpl : UnderwritingInvoke {

    override fun getPartnerIntegrated(): DTOPartnerIntegrated? {
        return RPCService.get<DTOPartnerIntegrated>(PartnerIntegratedRPCRequest())
    }
}