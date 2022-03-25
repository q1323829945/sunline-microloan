package cn.sunline.saas.loan.agreement.service

import cn.sunline.saas.fee.arrangement.service.FeeArrangementService
import cn.sunline.saas.interest.arrangement.service.InterestArrangementService
import cn.sunline.saas.loan.agreement.factory.LoanAgreementFactory
import cn.sunline.saas.loan.agreement.model.db.LoanAgreement
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementAdd
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementView
import cn.sunline.saas.loan.agreement.repository.LoanAgreementRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.arrangement.service.RepaymentArrangementService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * @title: LoanAgreementService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/1 11:21
 */
@Service
class LoanAgreementService(private val loanAgreementRepo: LoanAgreementRepository) :
    BaseMultiTenantRepoService<LoanAgreement, Long>(loanAgreementRepo) {
    @Autowired
    private lateinit var loanAgreementFactory: LoanAgreementFactory

    @Autowired
    private lateinit var interestArrangementService: InterestArrangementService

    @Autowired
    private lateinit var repaymentArrangementService: RepaymentArrangementService

    @Autowired
    private lateinit var feeArrangementService: FeeArrangementService

    @Transactional
    fun registered(dtoLoanAgreementAdd: DTOLoanAgreementAdd): DTOLoanAgreementView {
        val loanAgreement = save(loanAgreementFactory.instance(dtoLoanAgreementAdd))
        val interestArrangement = interestArrangementService.registered(
            loanAgreement.id, loanAgreement.term, dtoLoanAgreementAdd.interestArrangement
        )
        val repaymentArrangement =
            repaymentArrangementService.registered(loanAgreement.id, dtoLoanAgreementAdd.repaymentArrangement)
        val feeArrangement = feeArrangementService.registered(loanAgreement.id, dtoLoanAgreementAdd.feeArrangement)


        return DTOLoanAgreementView(loanAgreement, interestArrangement, repaymentArrangement, feeArrangement)
    }
}