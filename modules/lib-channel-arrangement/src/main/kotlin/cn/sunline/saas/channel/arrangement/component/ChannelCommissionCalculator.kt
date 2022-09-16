package cn.sunline.saas.channel.arrangement.component

import cn.sunline.saas.channel.arrangement.model.dto.RangeValue
import cn.sunline.saas.global.constant.CommissionMethodType
import java.math.BigDecimal

class ChannelCommissionCalculator(
    private val commissionMethodType: CommissionMethodType
) {

    fun calculate(range: BigDecimal, rangeValues: List<RangeValue>): BigDecimal? {
        return when (commissionMethodType) {
            CommissionMethodType.APPLY_COUNT_FIX_AMOUNT -> calculateApplyCountFixAmount(range, rangeValues)
            CommissionMethodType.APPROVAL_COUNT_FIX_AMOUNT -> calculateApprovalCountFixAmount(range, rangeValues)
            CommissionMethodType.APPLY_AMOUNT_RATIO -> calculateApplyAmountRatio(range, rangeValues)
            CommissionMethodType.APPROVAL_AMOUNT_RATIO -> calculateApprovalAmountRatio(range, rangeValues)
        }
    }


    private fun calculateApplyCountFixAmount(
        applyCount: BigDecimal,
        countRangeAmounts: List<RangeValue>
    ): BigDecimal? {
        return calculateRangeValue(applyCount, countRangeAmounts)
    }

    private fun calculateApprovalCountFixAmount(
        approvalCount: BigDecimal,
        countRangeAmounts: List<RangeValue>
    ): BigDecimal? {
        return calculateRangeValue(approvalCount, countRangeAmounts)
    }

    private fun calculateApplyAmountRatio(
        applyAmount: BigDecimal,
        amountRangeAmount: List<RangeValue>
    ): BigDecimal? {
        return calculateRangeValue(applyAmount, amountRangeAmount)
    }

    private fun calculateApprovalAmountRatio(
        approvalAmount: BigDecimal,
        amountRangeAmount: List<RangeValue>
    ): BigDecimal? {
        return calculateRangeValue(approvalAmount, amountRangeAmount)
    }


    private fun calculateRangeValue(
        range: BigDecimal,
        countRangeAmounts: List<RangeValue>
    ): BigDecimal? {
        countRangeAmounts.forEach {
            return if (it.lowerLimit == null && it.upperLimit == null) {
                it.rangeValue
            } else {
                if (it.lowerLimit != null && it.upperLimit == null && range > it.lowerLimit) {
                    it.rangeValue
                } else if (it.lowerLimit != null && it.upperLimit != null && range > it.lowerLimit && range < it.upperLimit) {
                    it.rangeValue
                } else {
                    it.rangeValue
                }
            }
        }
        return null
    }
}