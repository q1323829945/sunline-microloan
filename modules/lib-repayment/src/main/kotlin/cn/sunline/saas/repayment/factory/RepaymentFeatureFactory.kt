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

    fun instance(productId: Long, repaymentFeatureData: DTORepaymentFeatureAdd): RepaymentFeature {
        val repaymentFeatureId = seq.nextId()

        val repaymentFeatureModality = RepaymentFeatureModality(
            repaymentFeatureId,
            repaymentFeatureData.paymentMethod,
            repaymentFeatureData.frequency,
            repaymentFeatureData.repaymentDayType
        )

        val prepayments = mutableListOf<PrepaymentFeatureModality>()
        for (temp in repaymentFeatureData.prepaymentFeatureModality) {
            prepayments.add(
                PrepaymentFeatureModality(
                    seq.nextId(),
                    temp.term,
                    temp.type,
                    BigDecimal(temp.penaltyRatio)
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