package cn.sunline.saas.consumer.loan.service

import cn.sunline.saas.banking.transaction.service.BankingTransactionService
import cn.sunline.saas.loan.agreement.service.LoanAgreementService
import cn.sunline.saas.party.organisation.service.OrganisationService
import cn.sunline.saas.party.person.service.PersonService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * @title: ConsumerLoanService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/21 14:12
 */
@Service
class ConsumerLoanService {

//    @Autowired
//    private lateinit var consumerLoanPublish: ConsumerLoanPublish

    @Autowired
    private lateinit var loanAgreementService: LoanAgreementService

    @Autowired
    private lateinit var organisationService: OrganisationService

    @Autowired
    private lateinit var personService: PersonService

    @Autowired
    private lateinit var bankingTransactionService: BankingTransactionService

    fun underwriting() {
//        consumerLoanPublish.underwriting()
    }

    fun createLoanAgreement() {

        //TODO Save Customer Information

//        val dtoLoanAgreementAdd = DTOLoanAgreementAdd()
//        val loanAgreement = loanAgreementService.registered(dtoLoanAgreementAdd)

        //TODO Create Document

        //TODO Sign

        //TODO Communicate Customer

    }

    fun lending(){
        //TODO create lending instruction

        //TODO position keeping
        bankingTransactionService.registered()

        //TODO financial accounting
//        consumerLoanPublish.financialAccounting()
//
//        consumerLoanPublish.disbursement()
    }
}