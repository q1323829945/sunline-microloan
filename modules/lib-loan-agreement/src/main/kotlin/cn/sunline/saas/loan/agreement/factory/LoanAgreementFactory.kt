package cn.sunline.saas.loan.agreement.factory

import cn.sunline.saas.global.constant.AgreementStatus
import cn.sunline.saas.loan.agreement.model.db.LoanAgreement
import cn.sunline.saas.loan.agreement.model.db.LoanAgreementInvolvement
import cn.sunline.saas.loan.agreement.model.dto.DTOLoanAgreementAdd
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import cn.sunline.saas.seq.Sequence
import org.joda.time.Instant
import java.math.BigDecimal

/**
 * @title: LoanAgreementFactory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/22 19:09
 */
@Component
class LoanAgreementFactory {

    @Autowired
    private lateinit var seq: Sequence

    fun instance(dtoLoanAgreementAdd: DTOLoanAgreementAdd): LoanAgreement {
        val loanAgreementId = seq.nextId()
        val now = Instant.now()
        val term = dtoLoanAgreementAdd.term
        val involvements = mutableListOf<LoanAgreementInvolvement>()
        dtoLoanAgreementAdd.involvements.forEach {
            involvements.add(
                LoanAgreementInvolvement(id = seq.nextId(), partyId = it.partyId, involvementType = it.involvementType)
            )
        }
        return LoanAgreement(
            id = loanAgreementId,
            signedDate = now,
            fromDateTime = now,
            term = term,
            toDateTime = term.calDays(now),
            version = 1,
            status = AgreementStatus.OFFERED,
            amount = BigDecimal(dtoLoanAgreementAdd.amount),
            currency = dtoLoanAgreementAdd.currency,
            productId = dtoLoanAgreementAdd.productId,
            agreementDocument = null,
            involvements = involvements
        )
    }
}