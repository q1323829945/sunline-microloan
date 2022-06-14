package cn.sunline.saas.rpc.invoke

import cn.sunline.saas.dapr_wrapper.invoke.response.RPCResponse
import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.http.ResponseEntity

interface PdpaInvoke {
 fun getPDPAInformation(countryCode: String): RPCResponse<PDPAInformation>?

}