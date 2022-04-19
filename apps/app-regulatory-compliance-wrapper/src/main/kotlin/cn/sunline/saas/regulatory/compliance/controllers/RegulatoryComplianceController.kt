package cn.sunline.saas.regulatory.compliance.controllers

import cn.sunline.saas.regulatory.compliance.dto.DTORegulatoryCompliance
import cn.sunline.saas.regulatory.compliance.service.RegulatoryComplianceService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("RegulatoryCompliance")
class RegulatoryComplianceController {
    @Autowired
    private lateinit var regulatoryComplianceService: RegulatoryComplianceService
    @PostMapping
    fun getRegulatoryCompliance(@RequestBody(required = false) dtoRegulatoryCompliance: DTORegulatoryCompliance): Mono<Unit> {
        //TODO:
        println(dtoRegulatoryCompliance)
        return Mono.fromRunnable(){
            regulatoryComplianceService.getRegulatoryCompliance(dtoRegulatoryCompliance)
        }
    }
}