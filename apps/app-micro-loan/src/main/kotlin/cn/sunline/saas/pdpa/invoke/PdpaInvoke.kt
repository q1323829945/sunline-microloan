package cn.sunline.saas.pdpa.invoke

import cn.sunline.saas.pdpa.dto.PDPAInformation

interface PdpaInvoke {

 fun getPDPAInformation(countryCode: String): PDPAInformation?
}