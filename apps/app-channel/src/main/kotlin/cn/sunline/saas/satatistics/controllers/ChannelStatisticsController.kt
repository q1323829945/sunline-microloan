package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.Frequency
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.satatistics.service.ChannelStatisticsManagerService
import cn.sunline.saas.satatistics.service.dto.DTOChannelStatistics
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("channelStatistics")
class ChannelStatisticsController {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    @Autowired
    private lateinit var channelStatisticsManagerService: ChannelStatisticsManagerService

    @GetMapping("pie")
    fun getChannelGraphics(
        @RequestParam(required = false) startDate: String?,
        @RequestParam(required = false) endDate: String?,
        @RequestParam(required = false) tenantId: Long?,
        @RequestParam(required = false) channelCode: String?,
        @RequestParam(required = false) channelName: String?,
        @RequestParam(required = false) productId: String?,
        @RequestParam(required = false) applyStatus: ApplyStatus?,
        @RequestParam(required = false) frequency: Frequency?,
    ): ResponseEntity<DTOResponseSuccess<MutableList<DTOChannelStatistics>>> {
        val response = channelStatisticsManagerService.getChannelGraphics(
            startDate,
            endDate,
            tenantId,
            channelCode,
            channelName,
            productId,
            applyStatus,
            frequency,
            Pageable.unpaged()
        )
        return DTOResponseSuccess(response).response()

    }
}