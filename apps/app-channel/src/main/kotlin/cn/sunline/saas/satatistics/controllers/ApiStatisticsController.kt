package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.satatistics.service.ApiStatisticsManagerService
import cn.sunline.saas.satatistics.service.dto.DTOApiStatisticsCount
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping("ApiStatistics")
class ApiStatisticsController {

    @Autowired
    private lateinit var apiStatisticsManagerService: ApiStatisticsManagerService


    @GetMapping
    fun getStatisticsByDate(@PathParam("year") year:Long,
                            @PathParam("month") month:Long,
                            @PathParam("day") day:Long,
                            @PathParam("tenantId") tenantId:Long):DTOApiStatisticsCount{
        return apiStatisticsManagerService.getStatisticsByDate(year, month, day, tenantId)
    }
}