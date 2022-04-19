package cn.sunline.saas.disbursement.arrangement.factory

import cn.sunline.saas.disbursement.arrangement.model.db.DisbursementAccountType
import cn.sunline.saas.disbursement.arrangement.model.db.DisbursementArrangement
import cn.sunline.saas.disbursement.arrangement.model.dto.DTODisbursementArrangementAdd
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import cn.sunline.saas.seq.Sequence

/**
 * @title: DisbursementArrangementFactory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/18 16:08
 */
@Component
class DisbursementArrangementFactory(private val seq: Sequence) {

    fun instance(
        agreementId: Long,
        disbursementArrangementAdd: DTODisbursementArrangementAdd
    ): MutableList<DisbursementArrangement> {
        val disbursementArrangements = mutableListOf<DisbursementArrangement>()

        disbursementArrangements.add(
            DisbursementArrangement(
                id = seq.nextId(),
                agreementId = agreementId,
                disbursementAccountType = DisbursementAccountType.LENDING_ACCOUNT,
                disbursementAccount = disbursementArrangementAdd.lendingAccount.disbursementAccount,
                disbursementAccountBank = disbursementArrangementAdd.lendingAccount.disbursementAccountBank
            )
        )

        disbursementArrangementAdd.paymentAccount.forEach {
            disbursementArrangements.add(
                DisbursementArrangement(
                    id = seq.nextId(),
                    agreementId = agreementId,
                    disbursementAccountType = DisbursementAccountType.PAYMENT_ACCOUNT,
                    disbursementAccount = it.disbursementAccount,
                    disbursementAccountBank = it.disbursementAccountBank
                )
            )
        }
        return disbursementArrangements
    }
}