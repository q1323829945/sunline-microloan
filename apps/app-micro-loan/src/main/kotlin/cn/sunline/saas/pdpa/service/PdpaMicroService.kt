package cn.sunline.saas.pdpa.service

import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.rpc.invoke.PdpaInvoke
import cn.sunline.saas.rpc.invoke.dto.DTOPdpaAuthority
import cn.sunline.saas.rpc.invoke.dto.DTOPdpaView
import cn.sunline.saas.rpc.pubsub.PdpaPublish
import org.springframework.stereotype.Service

@Service
class PdpaMicroService(
    private val pdpaInvoke: PdpaInvoke,
    private val pdpaPublish: PdpaPublish
) {

    fun retrieve(country: CountryType, language: LanguageType): DTOPdpaView {
        return pdpaInvoke.getPDPAInformation(country, language)!!.data
    }

    fun getCustomerPdpaInformation(custoerId:String):DTOPdpaView?{
        val authority = pdpaInvoke.getCustomerPdpaAuthorityInformation(custoerId)!!

        return if(authority.pdpaId != null){
            pdpaInvoke.getPDPAInformation(authority.pdpaId)!!.data
        } else {
            null
        }
    }

    fun customerPDPAAuthorityWithdraw(customerId:String){
        pdpaPublish.customerPdpaWithdraw(customerId)
    }

    fun getPdpaAuthority():DTOPdpaAuthority{
        return pdpaInvoke.getPdpaAuthority()!!.data
    }
}
