package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.statistics.modules.dto.*
import cn.sunline.saas.statistics.services.CustomerDetailService
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
@RequestMapping("customer_detail")
class CustomerDetailController {

    @Autowired
    private lateinit var customerDetailService: CustomerDetailService

    @PostMapping
    fun addOne(@RequestBody dtoCustomerDetail: DTOCustomerDetail){
        customerDetailService.saveCustomerDetail(dtoCustomerDetail)
    }

    @GetMapping
    fun getListTest(@PathParam("startDate") startDate:String,
                    @PathParam("endDate") endDate:String):List<DTOCustomerCount>{
        return customerDetailService.getGroupByCustomerCount(
            DTOCustomerDetailQueryParams(
                startDateTime = DateTime.parse(startDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate(),
                endDateTime = DateTime.parse(endDate, DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate()
            )
        )
    }
}