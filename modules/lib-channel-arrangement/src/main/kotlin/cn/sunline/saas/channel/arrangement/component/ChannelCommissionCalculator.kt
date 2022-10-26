package cn.sunline.saas.channel.arrangement.component

import cn.sunline.saas.channel.arrangement.model.dto.RangeValue
import cn.sunline.saas.global.constant.CommissionMethodType
import java.math.BigDecimal

class ChannelCommissionCalculator(
    private val commissionMethodType: CommissionMethodType
) {

    fun calculate(range: BigDecimal, rangeValues: List<RangeValue>): BigDecimal? {
        return when (commissionMethodType) {
            CommissionMethodType.AMOUNT_RATIO -> calculateRatioAmount(range, rangeValues)
            CommissionMethodType.COUNT_FIX_AMOUNT -> calculateCountFixAmount(range, rangeValues)
        }
    }


    private fun calculateCountFixAmount(
        applyCount: BigDecimal,
        countRangeAmounts: List<RangeValue>
    ): BigDecimal? {
        return calculateRangeValue(applyCount, countRangeAmounts)
    }

    private fun calculateRatioAmount(
        approvalCount: BigDecimal,
        countRangeAmounts: List<RangeValue>
    ): BigDecimal? {
        return calculateRangeValue(approvalCount, countRangeAmounts)
    }


    private fun calculateRangeValue(
        range: BigDecimal,
        countRangeAmounts: List<RangeValue>
    ): BigDecimal? {
        var rangeValue = countRangeAmounts.firstOrNull()?.rangeValue
        countRangeAmounts.forEach {
            rangeValue = if (range > it.lowerLimit && range < it.upperLimit) {
                it.rangeValue
            } else {
                null
            }
        }
        return rangeValue
    }
}