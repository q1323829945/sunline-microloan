package cn.sunline.saas.satatistics.controllers

import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.response
import cn.sunline.saas.satatistics.service.LoanApplicationStatisticsManagerService
import cn.sunline.saas.statistics.modules.dto.DTOLoanApplicationDetail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("LoanApplicationStatistics")
class LoanApplicationStatisticsController {

    @Autowired
    private lateinit var loanApplicationStatisticsManagerService: LoanApplicationStatisticsManagerService

    @PostMapping("addLoanApplicationDetail")
    fun addLoanApplicationDetail(@RequestBody dtoLoanApplicationDetail: DTOLoanApplicationDetail){
        loanApplicationStatisticsManagerService.addLoanApplicationDetail(dtoLoanApplicationDetail)
    }

    @PostMapping("addLoanApplicationStatistics")
    fun addLoanApplicationStatistics(){
        loanApplicationStatisticsManagerService.addLoanApplicationStatistics()
    }


    @GetMapping
    fun getPaged(@RequestParam(required = false) year:Long?,
                            @RequestParam(required = false) month:Long?,
                            @RequestParam(required = false) day:Long?,
                            @RequestParam(required = false) tenantId:Long?,
                            pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val page = loanApplicationStatisticsManagerService.getPaged(year, month, day, tenantId, pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()

    }
}