package cn.sunline.saas.pdpa.invoke

import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.response.DTOResponseSuccess
import org.springframework.http.ResponseEntity

interface PdpaInvoke {
 fun getPDPAInformation(countryCode: String):DTOResponseSuccess<PDPAInformation>?

}