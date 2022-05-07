package cn.sunline.saas.consumer.loan.service

import cn.sunline.saas.banking.transaction.service.BankingTransactionService
import cn.sunline.saas.consumer.loan.invoke.ConsumerLoanInvoke
import cn.sunline.saas.consumer.loan.service.assembly.ConsumerLoanAssembly
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: ConsumerLoanService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 14:12
 */
@Service
class ConsumerLoanService(private val consumerLoanInvoke: ConsumerLoanInvoke) {

    private val objectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

//    @Autowired
//    private lateinit var consumerLoanPublish: ConsumerLoanPublish

    @Autowired
    private lateinit var loanAgreementService: LoanAgreementService

    @Autowired
    private lateinit var bankingTransactionService: BankingTransactionService

    fun underwriting() {
//        consumerLoanPublish.underwriting()
    }

    fun createLoanAgreement(applicationId: Long) {

        val customerOffer = consumerLoanInvoke.retrieveCustomerOffer(applicationId)
        val loanProduct = consumerLoanInvoke.retrieveLoanProduct(customerOffer.productId)

        val loanAgreementAggregate = loanAgreementService.registered(ConsumerLoanAssembly.convertToDTOLoanAgreementAdd(customerOffer,loanProduct))

        //TODO Create Document and update loan agreement's agreementDocument property with documentId
        val referenceDocument :String = ""

        val loanAgreement = loanAgreementAggregate.loanAgreement
        loanAgreement.agreementDocument = referenceDocument
        loanAgreementService.save(loanAgreement)

        // strategy is either offline or online
        // Offline Human participation
        // TODO management desk

        // Online Automatic
        //TODO Sign

        //TODO Communicate Customer

    }

    fun lending() {
        //TODO create lending instruction

        //TODO position keeping
        bankingTransactionService.registered()

        //TODO financial accounting
//        consumerLoanPublish.financialAccounting()
//
//        consumerLoanPublish.disbursement()
    }
}