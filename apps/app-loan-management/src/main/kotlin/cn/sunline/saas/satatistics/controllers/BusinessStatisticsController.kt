package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.satatistics.service.BusinessStatisticsManagerService
import cn.sunline.saas.satatistics.service.dto.DTOBusinessStatisticsCount
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
@RequestMapping("BusinessStatistics")
class BusinessStatisticsController {
    @Autowired
    private lateinit var businessDetailService: BusinessDetailService

    @Autowired
    private lateinit var businessStatisticsManagerService: BusinessStatisticsManagerService

    @PostMapping
    fun addOne(@RequestBody dtoBusinessDetail: DTOBusinessDetail){
        val businessDetail = businessDetailService.getFirstByAgreementId(dtoBusinessDetail.agreementId)
        businessDetail?.run {
            dtoBusinessDetail.transactionType = TransactionType.REPAYMENT
        }
        businessDetailService.saveBusinessDetail(dtoBusinessDetail)
    }

    @GetMapping
    fun getStatisticsByDate(@PathParam("year") year:Long,
                            @PathParam("month") month:Long,
                            @PathParam("day") day:Long,
                            @PathParam("tenantId") tenantId:Long):List<DTOBusinessStatisticsCount>{
        return businessStatisticsManagerService.getStatisticsByDate(year, month, day, tenantId)
    }
}