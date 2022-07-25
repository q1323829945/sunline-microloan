package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.dapr_wrapper.invoke.response.RPCResponse
import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.rpc.invoke.dto.DTOCustomerPdpaInformation
import cn.sunline.saas.rpc.invoke.dto.DTOPdpaAuthority
import cn.sunline.saas.rpc.invoke.dto.DTOPdpaView

interface PdpaInvoke {
 fun getPDPAInformation(country: CountryType, language: LanguageType): RPCResponse<DTOPdpaView>?

 fun getCustomerPdpaAuthorityInformation(customerId:String): DTOCustomerPdpaInformation?

 fun getPDPAInformation(pdpaId:String):RPCResponse<DTOPdpaView>?

 fun getPdpaAuthority():RPCResponse<DTOPdpaAuthority>?

}