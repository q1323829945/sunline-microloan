package cn.sunline.saas.fee.arrangement.component

import cn.sunline.saas.fee.arrangement.model.dto.DTOFeeArrangementView
import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.fee.util.FeeUtil
import cn.sunline.saas.global.constant.LoanFeeType
import java.math.BigDecimal


object FeeArrangementHelper {

    data class FeeDeductItem(val immediateFee: BigDecimal, val scheduleFee: BigDecimal)

    fun getDisbursementFeeDeductItem(feeArrangement: MutableList<DTOFeeArrangementView>?, amount: BigDecimal): FeeDeductItem {
        var feeImmediateAmount = BigDecimal.ZERO
        var feeScheduleAmount = BigDecimal.ZERO
        feeArrangement?.filter {
            it.feeType == LoanFeeType.DISBURSEMENT && it.feeDeductType == FeeDeductType.IMMEDIATE
        }?.forEach {
            feeImmediateAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    feeImmediateAmount.add(FeeUtil.calFeeAmount(amount, it.feeRate!!, it.feeMethodType))
                }
                FeeMethodType.FIX_AMOUNT -> {
                    feeImmediateAmount.add(it.feeAmount)
                }
                else -> {
                    feeImmediateAmount
                }
            }
        }

        feeArrangement?.filter {
            it.feeType == LoanFeeType.DISBURSEMENT && it.feeDeductType == FeeDeductType.SCHEDULE
        }?.forEach {
            feeScheduleAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    feeScheduleAmount.add(FeeUtil.calFeeAmount(amount, it.feeRate!!, it.feeMethodType))
                }
                FeeMethodType.FIX_AMOUNT -> {
                    feeScheduleAmount.add(it.feeAmount)
                }
                else -> {
                    feeScheduleAmount
                }
            }
        }

        return FeeDeductItem(feeImmediateAmount, feeScheduleAmount)
    }

    fun getPrepaymentFeeDeductItem(feeArrangement: MutableList<DTOFeeArrangementView>?, amount: BigDecimal): FeeDeductItem {
        var feeImmediateAmount = BigDecimal.ZERO
        val feeScheduleAmount = BigDecimal.ZERO
        feeArrangement?.filter {
            it.feeType == LoanFeeType.PREPAYMENT
        }?.forEach {
            feeImmediateAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    feeImmediateAmount.add(FeeUtil.calFeeAmount(amount, it.feeRate!!, it.feeMethodType))
                }
                FeeMethodType.FIX_AMOUNT -> {
                    feeImmediateAmount.add(it.feeAmount)
                }
                else -> {
                    feeImmediateAmount
                }
            }
        }

        return FeeDeductItem(feeImmediateAmount, feeScheduleAmount)
    }

    fun getOverdueFeeDeductItem(feeArrangement: MutableList<DTOFeeArrangementView>?, amount: BigDecimal): FeeDeductItem {
        var feeImmediateAmount = BigDecimal.ZERO
        var feeScheduleAmount = BigDecimal.ZERO
        feeArrangement?.filter {
            it.feeType == LoanFeeType.OVERDUE && it.feeDeductType == FeeDeductType.IMMEDIATE
        }?.forEach {
            feeImmediateAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    feeImmediateAmount.add(FeeUtil.calFeeAmount(amount, it.feeRate!!, it.feeMethodType))
                }
                FeeMethodType.FIX_AMOUNT -> {
                    feeImmediateAmount.add(it.feeAmount)
                }
                else -> {
                    feeImmediateAmount
                }
            }
        }

        feeArrangement?.filter {
            it.feeType == LoanFeeType.OVERDUE && it.feeDeductType == FeeDeductType.SCHEDULE
        }?.forEach {
            feeScheduleAmount = when (it.feeMethodType) {
                FeeMethodType.FEE_RATIO -> {
                    feeScheduleAmount.add(FeeUtil.calFeeAmount(amount, it.feeRate!!, it.feeMethodType))
                }
                FeeMethodType.FIX_AMOUNT -> {
                    feeScheduleAmount.add(it.feeAmount)
                }
                else -> {
                    feeScheduleAmount
                }
            }
        }

        return FeeDeductItem(feeImmediateAmount, feeScheduleAmount)
    }
}