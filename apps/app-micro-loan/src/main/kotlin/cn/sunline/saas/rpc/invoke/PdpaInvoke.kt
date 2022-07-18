package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.customer.offer.modules.dto.DTOPdpaView
import cn.sunline.saas.dapr_wrapper.invoke.response.RPCResponse
import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType

interface PdpaInvoke {
 fun getPDPAInformation(country: CountryType, language: LanguageType): RPCResponse<DTOPdpaView>?

}