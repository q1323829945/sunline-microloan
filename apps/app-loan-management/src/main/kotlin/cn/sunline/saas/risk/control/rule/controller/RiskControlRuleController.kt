package cn.sunline.saas.risk.control.rule.controller

import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.risk.control.rule.modules.RuleType
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleAdd
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleChange
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleSort
import cn.sunline.saas.risk.control.rule.modules.dto.DTORiskControlRuleView
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
    fun getALl(@RequestParam(required = false)ruleType: RuleType): ResponseEntity<DTOResponseSuccess<List<DTORiskControlRuleView>>> {
        val riskControlRuleList = riskControlRuleService.getAllControlRuleSort(ruleType)

        val responseEntity = objectMapper.convertValue<List<DTORiskControlRuleView>>(riskControlRuleList)

        return DTOResponseSuccess(responseEntity).response()
    }

    @PostMapping
    fun addOne(@RequestBody dtoRiskControlRuleAdd: DTORiskControlRuleAdd):ResponseEntity<DTOResponseSuccess<DTORiskControlRuleView>>{
        val riskControlRule = riskControlRuleService.addRiskControlRule(dtoRiskControlRuleAdd)

        val responseEntity = objectMapper.convertValue<DTORiskControlRuleView>(riskControlRule)

        return DTOResponseSuccess(responseEntity).response()
    }

    @PutMapping("{id}")
    fun updateOne(@PathVariable id: String,@RequestBody dtoRiskControlRuleChange: DTORiskControlRuleChange):ResponseEntity<DTOResponseSuccess<DTORiskControlRuleView>>{

        val riskControlRule = riskControlRuleService.updateRiskControlRule(id.toLong(),dtoRiskControlRuleChange)

        val responseEntity = objectMapper.convertValue<DTORiskControlRuleView>(riskControlRule)

        return DTOResponseSuccess(responseEntity).response()
    }

    @DeleteMapping("{id}")
    fun deleteOne(@PathVariable id: String):ResponseEntity<DTOResponseSuccess<Unit>>{

        riskControlRuleService.deleteRiskControlRule(id.toLong())

        return DTOResponseSuccess(Unit).response()
    }

    @PutMapping("sort")
    fun sort(@RequestBody sortList: DTORiskControlRuleSort):ResponseEntity<DTOResponseSuccess<Unit>>{

        riskControlRuleService.riskControlRuleSort(sortList.sortList)

        return DTOResponseSuccess(Unit).response()
    }

}