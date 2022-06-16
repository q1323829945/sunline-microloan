package cn.sunline.saas.consumer_loan.controllers

import cn.sunline.saas.consumer_loan.service.ConsumerLoanService
import cn.sunline.saas.consumer_loan.service.dto.*
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.invoice.service.dto.DTOInvoiceInfoView
import cn.sunline.saas.invoice.service.dto.DTOInvoiceRepay
import cn.sunline.saas.response.DTOResponseSuccess
import cn.sunline.saas.response.response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.random.Random

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


    data class DTOVerifyCode(
        val mobilePhone: String,
        val code: String
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
        val repaymentScheduleTrialResult = consumerLoanService.calculate(productId, amount.toBigDecimal(),term)
        return DTOResponseSuccess(repaymentScheduleTrialResult).response()
    }

    @PostMapping("/repaymentAgreement")
    fun addRepaymentAccount(@RequestBody dtoRepaymentAccountAdd: DTORepaymentAccountAdd): ResponseEntity<DTOResponseSuccess<MutableList<DTORepaymentAccountView>>>{
        val result = consumerLoanService.addRepaymentAccount(dtoRepaymentAccountAdd)
        return DTOResponseSuccess(result).response()
    }

    @PostMapping("/repay")
    fun repayEarly(@RequestBody dtoRepayEarly: DTORepayEarly): ResponseEntity<DTOResponseSuccess<DTORepayEarly>>{
        val result = consumerLoanService.repayEarly(dtoRepayEarly)
        return DTOResponseSuccess(result).response()
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
        var code = ""
        for(i in 1..6){
            val randoms = Random.nextInt(0,9)
            code += randoms
        }
        val result = DTOVerifyCode(
            mobilePhone = mobilePhone,
            code = code

        )
        return DTOResponseSuccess(result).response()
    }

    @PostMapping("/VerifyCode")
    fun verifyCode(@RequestBody dtoVerifyCode: DTOVerifyCode): ResponseEntity<DTOResponseSuccess<Any>> {
        return DTOResponseSuccess<Any>().response()
    }

    @PostMapping("/invoice/repay")
    fun repay(@RequestBody dtoInvoiceRepay: DTOInvoiceRepay): ResponseEntity<DTOResponseSuccess<MutableList<DTOInvoiceInfoView>>> {
        val response = consumerLoanService.repay(dtoInvoiceRepay)
        return DTOResponseSuccess(response).response()
    }
}