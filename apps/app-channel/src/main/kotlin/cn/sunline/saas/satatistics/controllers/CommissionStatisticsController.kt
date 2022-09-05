package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.satatistics.service.CommissionStatisticsManagerService
import cn.sunline.saas.satatistics.service.LoanApplicationStatisticsManagerService
import cn.sunline.saas.satatistics.service.dto.DTOApiStatisticsCount
import cn.sunline.saas.satatistics.service.dto.DTOCommissionStatisticsCharts
import cn.sunline.saas.satatistics.service.dto.DTOCommissionStatisticsCount
import cn.sunline.saas.channel.statistics.modules.dto.DTOCommissionDetail
import cn.sunline.saas.channel.statistics.modules.dto.DTOLoanApplicationDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.websocket.server.PathParam


@RestController
@RequestMapping("commissionStatistics")
class CommissionStatisticsController {

    @Autowired
    private lateinit var commissionStatisticsManagerService: CommissionStatisticsManagerService

    @PostMapping("addDetail")
    fun addCommissionDetail(@RequestBody dtoCommissionDetail: DTOCommissionDetail): ResponseEntity<DTOResponseSuccess<Any>> {
        commissionStatisticsManagerService.addCommissionDetail(dtoCommissionDetail)
        return DTOResponseSuccess<Any>().response()
    }

    @PostMapping("addStatistics")
    fun addCommissionStatistics(): ResponseEntity<DTOResponseSuccess<Any>> {
        commissionStatisticsManagerService.addCommissionStatistics()
        return DTOResponseSuccess<Any>().response()
    }

    @GetMapping
    fun getPaged(
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?,
        @RequestParam(required = false) tenantId: Long?,
        @RequestParam(required = false) channelCode: String?,
        @RequestParam(required = false) channelName: String?,
        @RequestParam(required = false) frequency: Frequency?,
        pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val page = commissionStatisticsManagerService.getPaged(
            startDate,
            endDate,
            tenantId,
            channelCode,
            channelName,
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
        @RequestParam(required = false) frequency: Frequency?,
    ): ResponseEntity<DTOResponseSuccess<DTOCommissionStatisticsCharts>> {
        val responseEntity = commissionStatisticsManagerService.getChartsPaged(
            startDate,
            endDate,
            tenantId,
            channelCode,
            channelName,
            frequency
        )
        return DTOResponseSuccess(responseEntity).response()

    }

    @GetMapping("statistics")
    fun getStatisticsByDate(@PathParam("year") year:Long,
                            @PathParam("month") month:Long,
                            @PathParam("day") day:Long,
                            @PathParam("tenantId") tenantId:Long): List<DTOCommissionStatisticsCount> {
        return commissionStatisticsManagerService.getStatisticsByDate(year, month, day, tenantId)
    }
}