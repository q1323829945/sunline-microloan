package cn.sunline.saas.risk.control.rule.controller

import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.dto.*
import cn.sunline.saas.risk.control.rule.services.RiskControlRuleService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/RiskControlRule")
class RiskControlRuleController {
    @Autowired
    private lateinit var riskControlRuleService: RiskControlRuleService

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @GetMapping
    fun getAll(@RequestParam(required = false)ruleType: RuleType): ResponseEntity<DTOResponseSuccess<List<DTORiskControlRuleGroup>>> {
        val riskControlRuleList = riskControlRuleService.getAllRiskControlRuleDetailSort(ruleType)
        return DTOResponseSuccess(objectMapper.convertValue<List<DTORiskControlRuleGroup>>(riskControlRuleList)).response()
    }

    @GetMapping("{id}")
    fun getDetail(@PathVariable id: String):ResponseEntity<DTOResponseSuccess<DTORiskControlRuleView>>{
        val riskControlRule = riskControlRuleService.getDetail(id.toLong())
        return DTOResponseSuccess(riskControlRule).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoRiskControlRuleAdd: DTORiskControlRuleAdd):ResponseEntity<DTOResponseSuccess<DTORiskControlRuleView>>{
        val riskControlRule = riskControlRuleService.addRiskControlRule(dtoRiskControlRuleAdd)
        return DTOResponseSuccess(riskControlRule).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: String,@RequestBody dtoRiskControlRuleChange: DTORiskControlRuleChange):ResponseEntity<DTOResponseSuccess<DTORiskControlRuleView>>{
        val riskControlRule = riskControlRuleService.updateRiskControlRule(id.toLong(),dtoRiskControlRuleChange)
        return DTOResponseSuccess(riskControlRule).response()
    }

    @DeleteMapping("{id}")
    fun deleteOne(@PathVariable id: String):ResponseEntity<DTOResponseSuccess<Unit>>{
        riskControlRuleService.deleteRiskControlRule(id.toLong())
        return DTOResponseSuccess(Unit).response()
    }

    @PutMapping("sort")
    @Deprecated("no sort")
    fun sort(@RequestBody sortList: DTORiskControlRuleSort):ResponseEntity<DTOResponseSuccess<Unit>>{
        riskControlRuleService.riskControlRuleSort(sortList.sortList)
        return DTOResponseSuccess(Unit).response()
    }

}