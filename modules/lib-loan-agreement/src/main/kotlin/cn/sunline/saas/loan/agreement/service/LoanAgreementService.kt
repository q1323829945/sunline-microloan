package cn.sunline.saas.loan.agreement.service

import cn.sunline.saas.disbursement.arrangement.service.DisbursementArrangementService
import cn.sunline.saas.fee.arrangement.service.FeeArrangementService
import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.interest.arrangement.exception.InterestArrangementNotFoundException
import cn.sunline.saas.interest.arrangement.service.InterestArrangementService
import cn.sunline.saas.invoice.arrangement.exception.InvoiceArrangementNotFoundException
import cn.sunline.saas.invoice.arrangement.service.InvoiceArrangementService
import cn.sunline.saas.loan.agreement.exception.LoanAgreementNotFoundException
import cn.sunline.saas.loan.agreement.factory.LoanAgreementFactory
import cn.sunline.saas.loan.agreement.model.db.LoanAgreement
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementAdd
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementView
import cn.sunline.saas.loan.agreement.repository.LoanAgreementRepository
import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.repayment.arrangement.exception.RepaymentArrangementNotFoundException
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

    @Autowired
    private lateinit var disbursementArrangementService: DisbursementArrangementService

    @Autowired
    private lateinit var invoiceArrangementService: InvoiceArrangementService

    @Transactional
    fun registered(dtoLoanAgreementAdd: DTOLoanAgreementAdd): DTOLoanAgreementView {
        val loanAgreement = save(loanAgreementFactory.instance(dtoLoanAgreementAdd))
        val interestArrangement = interestArrangementService.registered(
            loanAgreement.id, loanAgreement.term, dtoLoanAgreementAdd.interestArrangement
        )
        val repaymentArrangement =
            repaymentArrangementService.registered(loanAgreement.id, dtoLoanAgreementAdd.repaymentArrangement)

        val feeArrangement = dtoLoanAgreementAdd.feeArrangement?.run {
            feeArrangementService.registered(loanAgreement.id, this)
        }

        val disbursementArrangement = dtoLoanAgreementAdd.disbursementArrangement?.run {
            disbursementArrangementService.registered(loanAgreement.id, this)
        }

        val invoiceArrangement = invoiceArrangementService.registerInvoiceArrangement(
            loanAgreement.id,
            dtoLoanAgreementAdd.invoiceArrangement
        )

        return DTOLoanAgreementView(
            loanAgreement,
            interestArrangement,
            repaymentArrangement,
            feeArrangement,
            disbursementArrangement,
            invoiceArrangement
        )
    }

    fun archiveAgreement(loanAgreement: LoanAgreement): LoanAgreement {
        //TODO Create Document and update loan agreement's agreementDocument property with documentId
        val referenceDocument: String = ""

        loanAgreement.agreementDocument = referenceDocument
        return save(loanAgreement)
    }

    fun signAgreement(loanAgreement: LoanAgreement): LoanAgreement {
        // TODO sign component,the strategy is either offline or online
        // Offline Human participation
        // TODO management desk

        // Online Automatic
        //TODO Sign

        //TODO Communicate Customer

        // TODO udpate loan agreement status to change to SIGN
        loanAgreement.status = AgreementStatus.SIGNED
        return save(loanAgreement)
    }

    fun retrieve(loanAgreementId: Long): DTOLoanAgreementView {
        val loanAgreement = getOne(loanAgreementId)
        val interestArrangement = interestArrangementService.getOne(loanAgreementId)
        val repaymentArrangement = repaymentArrangementService.getOne(loanAgreementId)
        val feeArrangement = feeArrangementService.listByAgreementId(loanAgreementId)
        val disbursementArrangement = disbursementArrangementService.getOne(loanAgreementId)
        val invoiceArrangement = invoiceArrangementService.getOne(loanAgreementId)

        return DTOLoanAgreementView(
            loanAgreement ?: throw LoanAgreementNotFoundException("loan agreement not found"),
            interestArrangement ?: throw InterestArrangementNotFoundException("interest arrangement not found"),
            repaymentArrangement ?: throw RepaymentArrangementNotFoundException("repayment arrangement not found"),
            feeArrangement,
            disbursementArrangement,
            invoiceArrangement?:throw InvoiceArrangementNotFoundException("invoice arrangement not found")
        )
    }

    fun findByApplicationId(id: Long): LoanAgreement? {
        return loanAgreementRepo.findByApplicationId(id)
    }
}

