package cn.sunline.saas.loan.agreement.factory

import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.loan.agreement.model.LoanAgreementInvolvementType
import cn.sunline.saas.loan.agreement.model.db.LoanAgreement
import cn.sunline.saas.loan.agreement.model.db.LoanAgreementInvolvement
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementAdd
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import cn.sunline.saas.seq.Sequence
import java.math.BigDecimal

/**
 * @title: LoanAgreementFactory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/22 19:09
 */
@Component
class LoanAgreementFactory(
    private val tenantDateTime: TenantDateTime
) {

    @Autowired
    private lateinit var seq: Sequence

    fun instance(dtoLoanAgreementAdd: DTOLoanAgreementAdd): LoanAgreement {
        val loanAgreementId = seq.nextId()
        val now = tenantDateTime.now()
        val term = dtoLoanAgreementAdd.term
        val involvements = mutableListOf<LoanAgreementInvolvement>()
        dtoLoanAgreementAdd.lender.forEach {
            involvements.add(
                LoanAgreementInvolvement(
                    id = seq.nextId(),
                    partyId = it,
                    involvementType = LoanAgreementInvolvementType.LOAN_LENDER
                )
            )
        }
        involvements.add(
            LoanAgreementInvolvement(
                id = seq.nextId(),
                partyId = dtoLoanAgreementAdd.borrower,
                involvementType = LoanAgreementInvolvementType.LOAN_BORROWER
            )
        )
        return LoanAgreement(
            id = loanAgreementId,
            signedDate = now.toDate(),
            fromDateTime = now.toDate(),
            term = term,
            toDateTime = term.term.calDate(now).toDate(),
            version = 1,
            status = AgreementStatus.OFFERED,
            amount = BigDecimal(dtoLoanAgreementAdd.amount),
            currency = dtoLoanAgreementAdd.currency,
            productId = dtoLoanAgreementAdd.productId,
            agreementDocument = null,
            involvements = involvements,
            applicationId = dtoLoanAgreementAdd.applicationId,
            userId = dtoLoanAgreementAdd.userId,
            purpose = dtoLoanAgreementAdd.purpose
        )
    }
}