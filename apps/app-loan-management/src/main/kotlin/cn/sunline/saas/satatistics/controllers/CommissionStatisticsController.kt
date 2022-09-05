package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.satatistics.service.CommissionStatisticsManagerService
import cn.sunline.saas.satatistics.service.LoanApplicationStatisticsManagerService
import cn.sunline.saas.statistics.modules.dto.DTOCommissionDetail
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("CommissionStatistics")
class CommissionStatisticsController {

    @Autowired
    private lateinit var commissionStatisticsManagerService: CommissionStatisticsManagerService

    @PostMapping("addDetail")
    fun addCommissionDetail(@RequestBody dtoCommissionDetail: DTOCommissionDetail){
        commissionStatisticsManagerService.addCommissionDetail(dtoCommissionDetail)
    }

    @PostMapping("addStatistics")
    fun addCommissionStatistics(){
        commissionStatisticsManagerService.addCommissionStatistics()
    }

    @GetMapping
    fun getPaged(@RequestParam(required = false) year:Long?,
                 @RequestParam(required = false) month:Long?,
                 @RequestParam(required = false) day:Long?,
                 @RequestParam(required = false) tenantId:Long?,
                 pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val page = commissionStatisticsManagerService.getPaged(year, month, day, tenantId, pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()

    }
}