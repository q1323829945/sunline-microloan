package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.satatistics.service.CustomerStatisticsManagerService
import cn.sunline.saas.satatistics.service.dto.DTOCustomerStatisticsCount
import cn.sunline.saas.channel.statistics.modules.dto.*
import cn.sunline.saas.channel.statistics.services.CustomerDetailService
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping("CustomerStatistics")
class CustomerStatisticsController {

    @Autowired
    private lateinit var customerDetailService: CustomerDetailService

    @Autowired
    private lateinit var customerStatisticsManagerService: CustomerStatisticsManagerService

    @PostMapping
    fun addOne(@RequestBody dtoCustomerDetail: DTOCustomerDetail){
        customerDetailService.saveCustomerDetail(dtoCustomerDetail)
    }

    @GetMapping
    fun getStatisticsByDate(@PathParam("year") year:Long,
                            @PathParam("month") month:Long,
                            @PathParam("day") day:Long,
                            @PathParam("tenantId") tenantId:Long):DTOCustomerStatisticsCount{
        return customerStatisticsManagerService.getStatisticsByDate(year, month, day, tenantId)
    }
}