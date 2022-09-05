package cn.sunline.saas.loan_apply.controller

import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.global.constant.ProductType.*
import cn.sunline.saas.loan.service.LoanApplyService
import cn.sunline.saas.loan_apply.service.LoanApplyAppService
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("application")
class LoanApplicationController {
    @Autowired
    private lateinit var loanApplyService: LoanApplyService

    @Autowired
    private lateinit var loanApplyAppService: LoanApplyAppService

    data class DTOBatchSubmit(
        val data:Any
    )

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    @PostMapping("batch/record/loan")
    @Transactional(rollbackFor = [Exception::class])
    fun batchSubmit(@RequestBody dtoBatchSubmits: List<DTOBatchSubmit>):ResponseEntity<DTOResponseSuccess<Unit>>{
        dtoBatchSubmits.forEach {
            loanApplyAppService.loanRecord(objectMapper.writeValueAsString(it.data))
        }
        return DTOResponseSuccess(Unit).response()
    }
}