package cn.sunline.saas.pdpa.controller

import cn.sunline.saas.global.constant.LanguageType
import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.pdpa.service.PdpaMicroService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.rpc.invoke.dto.DTOPdpaAuthority
import cn.sunline.saas.rpc.invoke.dto.DTOPdpaView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("pdpa")
class PdpaController {

    @Autowired
    private lateinit var pdpaMicroService: PdpaMicroService

    @GetMapping("{country}/{language}/retrieve")
    fun getPDPAInformation(@PathVariable country: CountryType, @PathVariable language: LanguageType): ResponseEntity<DTOResponseSuccess<DTOPdpaView>> {
        return DTOResponseSuccess(pdpaMicroService.retrieve(country, language)).response()
    }

    @GetMapping("customer/{id}")
    fun getCustomerPdpaInformation(@PathVariable id:String): ResponseEntity<DTOResponseSuccess<DTOPdpaView>> {
        val pdpa = pdpaMicroService.getCustomerPdpaInformation(id)
        return DTOResponseSuccess(pdpa).response()
    }

    @PutMapping("customer/withdraw/{id}")
    fun customerPDPAAuthorityWithdraw(@PathVariable id:String): ResponseEntity<DTOResponseSuccess<Unit>>{
        pdpaMicroService.customerPDPAAuthorityWithdraw(id)
        return DTOResponseSuccess(Unit).response()
    }

    @GetMapping("authority")
    fun getPdpaAuthority():ResponseEntity<DTOResponseSuccess<DTOPdpaAuthority>>{
        val authority = pdpaMicroService.getPdpaAuthority()
        return DTOResponseSuccess(authority).response()
    }
}