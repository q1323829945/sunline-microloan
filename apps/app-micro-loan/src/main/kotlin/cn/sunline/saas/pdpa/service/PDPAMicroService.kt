package cn.sunline.saas.pdpa.service

import cn.sunline.saas.config.IpConfig
import cn.sunline.saas.customer.offer.modules.dto.DTOPdpaView
import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.rpc.invoke.PdpaInvoke
import org.springframework.stereotype.Service

@Service
class PDPAMicroService(
    private var ipConfig:IpConfig,
    private val pdpaInvoke: PdpaInvoke
) {

    fun retrieve(country: CountryType, language: LanguageType): DTOPdpaView {
        return pdpaInvoke.getPDPAInformation(country, language)!!.data
    }
}
