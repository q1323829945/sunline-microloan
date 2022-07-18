package cn.sunline.saas.consumer_loan.controllers

import cn.sunline.saas.consumer_loan.service.ConsumerLoanService
import cn.sunline.saas.consumer_loan.service.dto.*
import cn.sunline.saas.consumer_loan.service.dto.DTOLoanAgreementView
import cn.sunline.saas.consumer_loan.service.dto.DTORepaymentAccountAdd
import cn.sunline.saas.consumer_loan.service.dto.DTORepaymentScheduleTrialView
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.invoice.model.dto.DTOInvoiceInfoView
import cn.sunline.saas.invoice.model.dto.DTOInvoiceRepay
import cn.sunline.saas.invoice.model.dto.DTOPreRepaymentTrailView
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @title: ConsumerLoanController
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/3 14:09
 */
@RestController
@RequestMapping("/ConsumerLoan")
class ConsumerLoanController {

    data class DTOLoanAgreementStatus(
        val applicationId: String,
        val status: AgreementStatus
    )



    @Autowired
    private lateinit var consumerLoanService: ConsumerLoanService


    @PostMapping("/LoanAgreement/Initiate")
    fun initiateLoanAgreement(@RequestBody application:Long) {
        consumerLoanService.createLoanAgreement(application)
    }

    @PostMapping("/LoanAgreement/Sign")
    fun signLoanAgreementByOffline(@RequestBody agreementId:Long) {
        consumerLoanService.signLoanAgreementByOffline(agreementId)
    }


    @GetMapping("/LoanAgreement/{applicationId}")
    fun getLoanAgreementByApplicationId(@PathVariable applicationId:String):DTOLoanAgreementView?{
        return consumerLoanService.getLoanAgreementByApplicationId(applicationId.toLong())
    }

    @GetMapping("/LoanAgreement/{applicationId}/retrieve")
    fun getLoanAgreementInfoByApplicationId(@PathVariable applicationId:String):DTOLoanAgreementViewInfo?{
        return consumerLoanService.getLoanAgreementInfoByApplicationId(applicationId.toLong())
    }

    @GetMapping("/LoanAgreement/Info/{agreementId}/retrieve")
    fun getLoanAgreementInfoByAgreementId(@PathVariable agreementId:String):DTOLoanAgreementViewInfo?{
        return consumerLoanService.getLoanAgreementInfoByAgreementId(agreementId.toLong())
    }


    @PostMapping("/LoanAgreement/Signed")
    fun signedLoanAgreement(@RequestBody applicationId:Long){
        consumerLoanService.signedLoanAgreement(applicationId)
    }

    @PostMapping("/LoanAgreement/Paid")
    fun paidLoanAgreement(@RequestBody applicationId:Long){
        consumerLoanService.paidLoanAgreement(applicationId)
    }

    @GetMapping("{productId}/{amount}/{term}/calculate")
    fun calculate(@PathVariable productId:Long, @PathVariable amount:String, @PathVariable term: LoanTermType): ResponseEntity<DTOResponseSuccess<DTORepaymentScheduleTrialView>> {
        val repaymentScheduleTrialResult = consumerLoanService.calculateSchedule(productId, amount.toBigDecimal(),term)
        return DTOResponseSuccess(repaymentScheduleTrialResult).response()
    }

    @PostMapping("/repaymentAccount")
    fun addRepaymentAccount(@RequestBody dtoRepaymentAccountAdd: DTORepaymentAccountAdd): ResponseEntity<DTOResponseSuccess<DTORepaymentAccountView>>{
        val result = consumerLoanService.addRepaymentAccount(dtoRepaymentAccountAdd)
        return DTOResponseSuccess(result).response()
    }

    @PostMapping("/repay")
    fun prepayment(@RequestBody dtoPrepayment: DTOPrepayment){
        consumerLoanService.prepayment(dtoPrepayment)
    }

    @GetMapping("/LoanAgreement/detail/{agreementId}/retrieve")
    fun retrieveLoanAgreementDetail(@PathVariable agreementId: String): ResponseEntity<DTOResponseSuccess<DTOLoanAgreementDetailView>>{
        val result = consumerLoanService.retrieveLoanAgreementDetail(agreementId.toLong())
        return DTOResponseSuccess(result).response()
    }

    @GetMapping("/bankList/retrieve")
    fun retrieveBankList(): ResponseEntity<DTOResponseSuccess<MutableList<DTOBankListView>>>{
        val result = consumerLoanService.retrieveBankList()
        return DTOResponseSuccess(result).response()
    }


    @GetMapping("/VerifyCode/{mobilePhone}")
    fun getVerifyCode(@PathVariable mobilePhone: String): ResponseEntity<DTOResponseSuccess<DTOVerifyCode>> {
        val result = consumerLoanService.getVerifyCode(mobilePhone)
        return DTOResponseSuccess(result).response()
    }

    @PostMapping("/VerifyCode")
    fun verifyCode(@RequestBody dtoVerifyCode: DTOVerifyCode): ResponseEntity<DTOResponseSuccess<Any>> {
        return DTOResponseSuccess<Any>().response()
    }

    @PostMapping("/invoice/repay")
    fun repay(@RequestBody dtoInvoiceRepay: DTOInvoiceRepay): ResponseEntity<DTOResponseSuccess<DTOInvoiceInfoView>> {
        val response = consumerLoanService.repayment(dtoInvoiceRepay)
        return DTOResponseSuccess(response).response()
    }

    @GetMapping("/prepayment/{agreementId}/calculate")
    fun calculatePrepayment(@PathVariable("agreementId") agreementId: Long): ResponseEntity<DTOResponseSuccess<DTOPreRepaymentTrailView>> {
        val trailView = consumerLoanService.calculatePrepayment(agreementId)
        return DTOResponseSuccess(trailView).response()
    }

    @GetMapping("/repaymentAccount/{agreementId}/retrieve")
    fun getRepaymentAccounts(@PathVariable("agreementId") agreementId: Long): ResponseEntity<DTOResponseSuccess<DTORepaymentAccountView>> {
        val trailView = consumerLoanService.getRepaymentAccounts(agreementId)
        return DTOResponseSuccess(trailView).response()
    }

    @PostMapping("/repayment/instruction/fulfill")
    fun fulfillLoanInvoiceRepayment(@RequestBody instructionId:Long){
        consumerLoanService.fulfillLoanInvoiceRepayment(instructionId)
    }

    @PostMapping("/repayment/instruction/fail")
    fun failLoanInvoiceRepayment(@RequestBody instructionId:Long){
        consumerLoanService.failLoanInvoiceRepayment(instructionId)
    }

    @GetMapping("/repayment/record/{customerId}/retrieve")
    fun getRepaymentInstructionRecord(@PathVariable("customerId", required = false) customerId: Long): ResponseEntity<DTOResponseSuccess<MutableList<DTORepaymentRecordView>>>{
        val view = consumerLoanService.getRepaymentInstructionRecord(customerId)
        return DTOResponseSuccess(view).response()
    }
}