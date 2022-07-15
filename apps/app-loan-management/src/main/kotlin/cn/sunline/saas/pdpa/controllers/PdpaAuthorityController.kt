package cn.sunline.saas.pdpa.controllers

import cn.sunline.saas.pdpa.modules.dto.DTOPdpaAuthority
import cn.sunline.saas.pdpa.services.PdpaAuthorityService
import cn.sunline.saas.response.DTOResponseSuccess
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("pdpa/authority")
class PdpaAuthorityController {
    @Autowired
    private lateinit var pdpaAuthorityService: PdpaAuthorityService
    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @GetMapping
    fun getOne():ResponseEntity<DTOResponseSuccess<DTOPdpaAuthority>>{
        val authority = pdpaAuthorityService.getOne()
        return objectMapper.convertValue(authority)
    }

    @PutMapping
    fun updateOne(@RequestBody dtoPdpaauthority: DTOPdpaAuthority):ResponseEntity<DTOResponseSuccess<DTOPdpaAuthority>>{
        val authority = pdpaAuthorityService.updateOne(dtoPdpaauthority)
        return objectMapper.convertValue(authority)
    }
}