package cn.sunline.saas.repayment.arrangement.factory

import cn.sunline.saas.repayment.arrangement.model.db.PrepaymentArrangement
import cn.sunline.saas.repayment.arrangement.model.db.RepaymentArrangement
import cn.sunline.saas.repayment.arrangement.model.dto.DTORepaymentArrangementAdd
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * @title: RepaymentArrangementFactory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/24 14:54
 */
@Component
class RepaymentArrangementFactory {
    @Autowired
    private lateinit var seq: Sequence

    fun instance(agreementId: Long, dtoRepaymentArrangementAdd: DTORepaymentArrangementAdd): RepaymentArrangement {
        val prepaymentArrangement = mutableListOf<PrepaymentArrangement>()

        dtoRepaymentArrangementAdd.prepaymentArrangement.forEach {
            prepaymentArrangement.add(
                PrepaymentArrangement(
                    id = seq.nextId(),
                    term = it.term,
                    type = it.type,
                    penaltyRatio = it.penaltyRatio
                )
            )
        }

        return RepaymentArrangement(
            id = agreementId,
            paymentMethod = dtoRepaymentArrangementAdd.paymentMethod,
            frequency = dtoRepaymentArrangementAdd.frequency,
            repaymentDayType = dtoRepaymentArrangementAdd.repaymentDayType,
            prepayment = prepaymentArrangement
        )

    }

}