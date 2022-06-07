package cn.sunline.saas.statistics.controllers

import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.statistics.modules.dto.DTOApiDetailApiCount
import cn.sunline.saas.statistics.modules.dto.DTOApiDetailQueryDate
import cn.sunline.saas.statistics.services.ApiDetailService
import org.joda.time.DateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api")
class ApiDetailController {

    @Autowired
    private lateinit var apiDetailService: ApiDetailService

    @PostMapping
    fun addOne(@RequestBody api:String){
        apiDetailService.saveApiDetail(api)
    }

    @GetMapping("{tenantId}")
    fun getList(@PathVariable tenantId:Long):List<DTOApiDetailApiCount>{
        val time = DateTime.now()
        return apiDetailService.getGroupByApiCount(
            DTOApiDetailQueryDate(
                startDateTime = time.plusDays(-1).toDate(),
                endDateTime = time.toDate(),
                tenantId
            )
        )
    }


}