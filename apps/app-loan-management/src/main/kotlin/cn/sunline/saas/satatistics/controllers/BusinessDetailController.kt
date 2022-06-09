package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.statistics.modules.TransactionType
import cn.sunline.saas.statistics.modules.dto.DTOBusinessCount
import cn.sunline.saas.statistics.modules.dto.DTOBusinessDetail
import cn.sunline.saas.statistics.modules.dto.DTOBusinessDetailQueryParams
import cn.sunline.saas.statistics.services.BusinessDetailService
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
@RequestMapping("business_detail")
class BusinessDetailController {
    @Autowired
    private lateinit var businessDetailService: BusinessDetailService

    @PostMapping
    fun addOne(@RequestBody dtoBusinessDetail: DTOBusinessDetail){
        val businessDetail = businessDetailService.getFirstByAgreementId(dtoBusinessDetail.agreementId)
        businessDetail?.run {
            dtoBusinessDetail.transactionType = TransactionType.REPAYMENT
        }
        businessDetailService.saveBusinessDetail(dtoBusinessDetail)
    }

    @GetMapping
    fun getList(@PathParam("startDate") startDate:String,
                @PathParam("endDate") endDate:String):List<DTOBusinessCount>{
        return businessDetailService.getGroupByBusinessCount(
            DTOBusinessDetailQueryParams(
                startDateTime = DateTime.parse(startDate,DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate(),
                endDateTime = DateTime.parse(endDate,DateTimeFormat.forPattern("yyyyMMddHHmmss")).toDate()
            )
        )
    }
}