package cn.sunline.saas.consumer_loan.controller

import cn.sunline.saas.consumer_loan.service.LoanAgreementManagerService
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("LoanAgreement")
class LoanAgreementController {


    @Autowired
    private lateinit var loanAgreementManagerService: LoanAgreementManagerService

    @GetMapping
    fun getPaged(pageable: Pageable):ResponseEntity<DTOPagedResponseSuccess>{
        val page = loanAgreementManagerService.getPaged(pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @PutMapping("{status}/{id}")
    fun paid(@PathVariable(name = "id") id:String,@PathVariable(name = "status")status:AgreementStatus):ResponseEntity<DTOResponseSuccess<Unit>>{
        loanAgreementManagerService.paid(id, status)

        return DTOResponseSuccess(Unit).response()

    }
}