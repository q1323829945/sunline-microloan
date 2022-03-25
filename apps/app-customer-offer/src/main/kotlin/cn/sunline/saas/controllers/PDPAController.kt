package cn.sunline.saas.controllers

import cn.sunline.saas.global.model.Country
import cn.sunline.saas.pdpa.factory.PDPAFactory
import cn.sunline.saas.pdpa.modules.dto.PDPAInformationView
import cn.sunline.saas.pdpa.services.PDPAService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("pdpa")
class PDPAController {

    @Autowired
    private lateinit var pdpaFactory: PDPAFactory

    @Autowired
    private lateinit var pdpaService: PDPAService


    @GetMapping("{countryCode}/retrieve")
    fun getPDPAInformation(@PathVariable countryCode: String): ResponseEntity<DTOResponseSuccess<PDPAInformationView>> {

        val pdpa = pdpaFactory.getInstance(Country.valueOf(countryCode)).getPDPA()

        return DTOResponseSuccess(pdpa).response()
    }
}