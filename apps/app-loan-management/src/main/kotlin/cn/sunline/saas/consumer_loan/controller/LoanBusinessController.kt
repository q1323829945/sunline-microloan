package cn.sunline.saas.consumer_loan.controller

import cn.sunline.saas.consumer_loan.service.LoanBusinessManagerService
import cn.sunline.saas.party.person.model.PersonIdentificationType
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("LoanBusiness")
class LoanBusinessController {


    @Autowired
    private lateinit var loanBusinessManagerService: LoanBusinessManagerService

    @GetMapping
    fun getPaged(
        @RequestParam(required = true) identificationNo: String,
        @RequestParam(required = true) identificationType: PersonIdentificationType,
        pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val page = loanBusinessManagerService.getPaged(identificationNo, identificationType,pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @GetMapping("application/{applicationId}/retrieve")
    fun getApplicationLoanPaged(@PathVariable applicationId: String,pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>{
        val page = loanBusinessManagerService.getLoanApplicationPaged(applicationId,pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @GetMapping("fee/{applicationId}/retrieve")
    fun getFeeItemPagedPaged(@PathVariable applicationId: String,pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>{
        val page = loanBusinessManagerService.getFeeItemPaged(applicationId,pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @GetMapping("disbursement/{agreementId}/retrieve")
    fun getLoanDisbursementPaged(@PathVariable agreementId: String,pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>{
        val page = loanBusinessManagerService.getLoanDisbursementPaged(agreementId,pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @GetMapping("history/event/{applicationId}/retrieve")
    fun getLoanHistoryEventPaged(@PathVariable applicationId: String,pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>{
        val page = loanBusinessManagerService.getLoanHistoryEventPaged(applicationId,pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }

    @GetMapping("repayment/record/{agreementId}/retrieve")
    fun getRepaymentRecordPaged(@PathVariable agreementId: String,pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess>{
        val page = loanBusinessManagerService.getRepaymentRecordPaged(agreementId,pageable)
        return DTOPagedResponseSuccess(page.map { it }).response()
    }
}