package cn.sunline.saas.consumer.loan.controllers

import cn.sunline.saas.consumer.loan.service.ConsumerLoanService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
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

    @Autowired
    private lateinit var consumerLoanService: ConsumerLoanService

    @PostMapping("/Underwriting")
    fun initiate() {
        consumerLoanService.underwriting()
    }


    @PostMapping("/LoanAgreement")
    fun initiateLoanAgreement() {
        consumerLoanService.createLoanAgreement()
    }

    @PostMapping("/Lending")
    fun disbursement() {
        consumerLoanService.lending()
    }


}