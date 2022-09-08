package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.satatistics.service.LoanApplicationStatisticsManagerService
import cn.sunline.saas.satatistics.service.dto.DTOLoanApplicationStatisticsCharts
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("LoanApplicationStatistics")
class LoanApplicationStatisticsController {

    @Autowired
    private lateinit var loanApplicationStatisticsManagerService: LoanApplicationStatisticsManagerService

    @PostMapping("addDetail")
    fun addLoanApplicationDetail(@RequestBody dtoLoanApplicationDetail: DTOLoanApplicationDetail): ResponseEntity<DTOResponseSuccess<Any>> {
        loanApplicationStatisticsManagerService.addLoanApplicationDetail(dtoLoanApplicationDetail)
        return DTOResponseSuccess<Any>().response()
    }

    @PostMapping("addStatistics")
    fun addLoanApplicationStatistics(): ResponseEntity<DTOResponseSuccess<Any>> {
        loanApplicationStatisticsManagerService.addLoanApplicationStatistics()
        return DTOResponseSuccess<Any>().response()
    }


    @GetMapping
    fun getPaged(
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?,
        @RequestParam(required = false) tenantId: Long?,
        @RequestParam(required = false) channelCode: String?,
        @RequestParam(required = false) channelName: String?,
        @RequestParam(required = false) productId: String?,
        @RequestParam(required = false) frequency: Frequency?,
        pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val page = loanApplicationStatisticsManagerService.getPaged(
            startDate,
            endDate,
            tenantId,
            channelCode,
            channelName,
            productId,
            frequency,
            pageable
        )
        return DTOPagedResponseSuccess(page.map { it }).response()

    }

    @GetMapping("charts")
    fun getChartsPaged(
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?,
        @RequestParam(required = false) tenantId: Long?,
        @RequestParam(required = false) channelCode: String?,
        @RequestParam(required = false) channelName: String?,
        @RequestParam(required = false) productId: String?,
        @RequestParam(required = false) frequency: Frequency?,
    ): ResponseEntity<DTOResponseSuccess<DTOLoanApplicationStatisticsCharts>> {
        val responseEntity = loanApplicationStatisticsManagerService.getChartsPaged(
            startDate,
            endDate,
            tenantId,
            channelCode,
            channelName,
            productId,
            frequency
        )
        return DTOResponseSuccess(responseEntity).response()

    }
}