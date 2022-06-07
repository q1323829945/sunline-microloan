package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.statistics.modules.dto.*
import cn.sunline.saas.statistics.services.CustomerDetailService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("customer_detail")
class CustomerDetailController {

    @Autowired
    private lateinit var customerDetailService: CustomerDetailService

    @PostMapping
    fun addOne(@RequestBody dtoCustomerDetail: DTOCustomerDetail){
        customerDetailService.saveCustomerDetail(dtoCustomerDetail)
    }

    @GetMapping("{tenantId}")
    fun getListTest(@PathVariable tenantId:Long):List<DTOCustomerCount>{
        val time = DateTime.now()
        return customerDetailService.getGroupByCustomerCount(
            DTOBusinessDetailQueryParams(
                startDateTime = time.plusDays(-1).toDate(),
                endDateTime = time.toDate(),
                tenantId
            )
        )
    }
}