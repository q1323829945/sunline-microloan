package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.statistics.modules.dto.DTOApiCount
import cn.sunline.saas.statistics.modules.dto.DTOApiDetailQueryParams
import cn.sunline.saas.statistics.services.ApiDetailService
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping("api_detail")
class ApiDetailController {

    @Autowired
    private lateinit var apiDetailService: ApiDetailService

    @GetMapping
    fun getList(@PathParam("startDate") startDate:String,
                    @PathParam("endDate") endDate:String):List<DTOApiCount>{
        return apiDetailService.getGroupByApiCount(
            DTOApiDetailQueryParams(
                startDateTime = DateTime.parse(startDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate(),
                endDateTime = DateTime.parse(endDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate()
            )
        )
    }
}