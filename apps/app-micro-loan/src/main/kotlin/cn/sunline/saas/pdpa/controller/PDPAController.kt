package cn.sunline.saas.pdpa.controller

import cn.sunline.saas.pdpa.dto.PDPAInformation
import cn.sunline.saas.pdpa.service.PDPAMicroService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("pdpa")
class PDPAController {

    @Autowired
    private lateinit var pdpaMicroService: PDPAMicroService

    @GetMapping("{countryCode}/retrieve")
    fun getPDPAInformation(@PathVariable countryCode: String): ResponseEntity<DTOResponseSuccess<PDPAInformation>> {
        return DTOResponseSuccess(pdpaMicroService.retrieve(countryCode)).response()
    }

}