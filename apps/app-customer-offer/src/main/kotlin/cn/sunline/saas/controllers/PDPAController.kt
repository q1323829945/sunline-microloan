package cn.sunline.saas.controllers

import cn.sunline.saas.global.model.Country
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.services.PDPAService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import cn.sunline.saas.services.PDPAService.PDPAInformation

@RestController
@RequestMapping("pdpa")
class PDPAController {

    @Autowired
    private lateinit var pdpaService: PDPAService

    @GetMapping("{countryCode}/retrieve")
    fun getPDPAInformation(@PathVariable countryCode: String): ResponseEntity<DTOResponseSuccess<PDPAInformation>> {
        val pdpa = pdpaService.retrieve(countryCode)
        return DTOResponseSuccess(pdpa).response()
    }

}