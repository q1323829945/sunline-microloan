package cn.sunline.saas.channel.arrangement.component

import cn.sunline.saas.channel.arrangement.model.dto.RangeValue
import cn.sunline.saas.global.constant.CommissionMethodType
import java.math.BigDecimal

class ChannelCommissionCalculator(
    private val commissionMethodType: CommissionMethodType
) {

    data class CommissionData(val commission: BigDecimal, val ratio: BigDecimal?)

    fun calculate(
        count: String,
        statisticsAmount: BigDecimal,
        rangeValues: List<RangeValue>
    ): CommissionData {
        return when (commissionMethodType) {

            CommissionMethodType.COUNT_FIX_AMOUNT -> {
                val commission = getCommissionAmountOrRatio(
                    count.toBigDecimal(),
                    rangeValues
                ) ?: BigDecimal.ZERO
                CommissionData(commission, null)
            }

            CommissionMethodType.AMOUNT_RATIO -> {
                val ratio = getCommissionAmountOrRatio(statisticsAmount, rangeValues) ?: BigDecimal.ZERO
                val commission = statisticsAmount.multiply(ratio)
                CommissionData(commission, ratio)
            }
        }
    }

    private fun getCommissionAmountOrRatio(range: BigDecimal, rangeValues: List<RangeValue>): BigDecimal? {
        return when (commissionMethodType) {
            CommissionMethodType.AMOUNT_RATIO -> getAmountRatio(range, rangeValues)
            CommissionMethodType.COUNT_FIX_AMOUNT -> getCountFixAmount(range, rangeValues)
        }
    }


    private fun getCountFixAmount(
        count: BigDecimal,
        rangeAmounts: List<RangeValue>
    ): BigDecimal? {
        return getRangeValue(count, rangeAmounts)
    }

    private fun getAmountRatio(
        approvalCount: BigDecimal,
        rangeAmounts: List<RangeValue>
    ): BigDecimal? {
        return getRangeValue(approvalCount, rangeAmounts)
    }


    private fun getRangeValue(
        range: BigDecimal,
        countRangeAmounts: List<RangeValue>
    ): BigDecimal? {
        var rangeValue = countRangeAmounts.firstOrNull()?.rangeValue
        countRangeAmounts.forEach {
            rangeValue = if (range > it.lowerLimit && range < it.upperLimit) {
                return it.rangeValue
            } else {
                null
            }
        }
        return rangeValue
    }
}