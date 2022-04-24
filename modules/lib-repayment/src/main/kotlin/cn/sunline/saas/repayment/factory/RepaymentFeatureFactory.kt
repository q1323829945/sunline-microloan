package cn.sunline.saas.repayment.factory

import cn.sunline.saas.repayment.model.db.PrepaymentFeatureModality
import cn.sunline.saas.repayment.model.db.RepaymentFeature
import cn.sunline.saas.repayment.model.db.RepaymentFeatureModality
import cn.sunline.saas.repayment.model.dto.DTORepaymentFeatureAdd
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.math.BigDecimal

/**
 * @title: RepaymentProductFeatureFactory
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/11 14:24
 */
@Component
class RepaymentFeatureFactory {

    @Autowired
    private lateinit var seq: Sequence

    fun instance(productId: Long, interestFeatureData: DTORepaymentFeatureAdd): RepaymentFeature {
        val repaymentFeatureId = seq.nextId()

        val repaymentFeatureModality = RepaymentFeatureModality(
            repaymentFeatureId,
            interestFeatureData.paymentMethod,
            interestFeatureData.frequency,
            interestFeatureData.repaymentDayType
        )

        val prepayments = mutableListOf<PrepaymentFeatureModality>()
        for (temp in interestFeatureData.prepaymentFeatureModality) {
            val penaltyRatio = temp.penaltyRatio ?: BigDecimal.ZERO

            prepayments.add(
                PrepaymentFeatureModality(
                    seq.nextId(),
                    temp.term,
                    temp.type,
                    penaltyRatio
                )
            )
        }

        return RepaymentFeature(
            repaymentFeatureId,
            productId,
            repaymentFeatureModality,
            prepayments
        )
    }
}