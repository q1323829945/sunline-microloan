package cn.sunline.saas.underwriting.controllers

import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.services.RiskControlService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping("RiskControlReport")
class RiskControlReportController {

    @Autowired
    private lateinit var riskControlService: RiskControlService

    @GetMapping
    fun getReport(@PathParam("id")id:String):ResponseEntity<DTOResponseSuccess<RiskControlService.ExecuteResult>>{
        //TODO:
        val result = riskControlService.execute(id.toLong(),RuleType.BUSINESS)
        return DTOResponseSuccess(result).response()
    }
}