package cn.sunline.saas.consumer_loan.controllers

import cn.sunline.saas.consumer_loan.service.ConsumerLoanService
import cn.sunline.saas.consumer_loan.service.dto.DTOLoanAgreementView
import cn.sunline.saas.global.constant.AgreementStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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


    @GetMapping("{applicationId}")
    fun getLoanAgreementByApplicationId(@PathVariable applicationId:String):DTOLoanAgreementView{
        return consumerLoanService.getLoanAgreementByApplicationId(applicationId.toLong())
    }


    @PostMapping("status")
    fun updateLoanAgreementStatus(@RequestBody dtoLoanAgreementStatus:DTOLoanAgreementStatus){
        consumerLoanService.updateLoanAgreementStatus(dtoLoanAgreementStatus.applicationId.toLong(),dtoLoanAgreementStatus.status)
    }
}