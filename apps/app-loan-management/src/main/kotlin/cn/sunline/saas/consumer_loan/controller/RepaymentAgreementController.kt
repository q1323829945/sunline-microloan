package cn.sunline.saas.consumer_loan.controller

import cn.sunline.saas.consumer_loan.service.RepaymentAgreementManagerService
import cn.sunline.saas.money.transfer.instruction.model.InstructionLifecycleStatus
import cn.sunline.saas.money.transfer.instruction.model.MoneyTransferInstructionType
import cn.sunline.saas.response.DTOPagedResponseSuccess
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/ConsumerLoan")
class RepaymentAgreementController {

    @Autowired
     private lateinit var repaymentAgreementManagerService: RepaymentAgreementManagerService

    @GetMapping("/repayment/instruction")
    fun getPaged(@RequestParam(required = false)agreementId:String? = null,
                 @RequestParam(required = false)customerId: String? = null,
                 pageable: Pageable
    ): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = repaymentAgreementManagerService.getPaged(agreementId,customerId,MoneyTransferInstructionType.REPAYMENT,InstructionLifecycleStatus.PREPARED,pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @GetMapping("/invoice/{customerId}/history")
    fun getHistoryPaged(@PathVariable customerId:String, pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = repaymentAgreementManagerService.getHistoryPaged(customerId.toLong(),pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }

    @PutMapping("/repayment/instruction/FULFILLED/{id}")
    fun fulfillLoanInvoiceRepayment(@PathVariable(name = "id") id:String):ResponseEntity<DTOResponseSuccess<Unit>>{
        repaymentAgreementManagerService.fulfillLoanInvoiceRepayment(id)
        return DTOResponseSuccess(Unit).response()
    }

    @PutMapping("/repayment/instruction/FAILED/{id}")
    fun failLoanInvoiceRepayment(@PathVariable(name = "id") id:String):ResponseEntity<DTOResponseSuccess<Unit>>{
        repaymentAgreementManagerService.failLoanInvoiceRepayment(id)
        return DTOResponseSuccess(Unit).response()
    }

    @GetMapping("/invoice/detail/{invoiceId}/retrieve")
    fun getInvoiceDetail(@PathVariable invoiceId:String, pageable: Pageable): ResponseEntity<DTOPagedResponseSuccess> {
        val paged = repaymentAgreementManagerService.getInvoiceDetail(invoiceId.toLong(),pageable)
        return DTOPagedResponseSuccess(paged.map { it }).response()
    }
}