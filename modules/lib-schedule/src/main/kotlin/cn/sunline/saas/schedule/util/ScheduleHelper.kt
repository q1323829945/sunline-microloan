package cn.sunline.saas.schedule.util

import cn.sunline.saas.schedule.Schedule
import java.math.BigDecimal


object ScheduleHelper {

    data class DTORepaymentScheduleTrialView(
        val installment: BigDecimal? = null,
        val fee: BigDecimal,
        val interestRate: BigDecimal,
        val schedule: MutableList<DTORepaymentScheduleDetailTrialView>
    )

    data class DTORepaymentScheduleDetailTrialView(
        val period: Int,
        val repaymentDate: String,
        val installment: BigDecimal,
        val principal: BigDecimal,
        val interest: BigDecimal,
        val fee: BigDecimal
    )


    fun convertToScheduleTrialMapper(
        dtoSchedule: MutableList<Schedule>,
        immediateFee: BigDecimal
    ): DTORepaymentScheduleTrialView {
        val dtoRepaymentScheduleDetailTrialView: MutableList<DTORepaymentScheduleDetailTrialView> = ArrayList()

        val interestRate = dtoSchedule.first().interestRate
        var installment = dtoSchedule.first().instalment
        for (schedule in dtoSchedule) {
//            if (installment != schedule.instalment) {
//                installment = BigDecimal.ZERO.setScale(CalculatePrecision.AMOUNT, RoundingMode.HALF_UP)
//            }
            dtoRepaymentScheduleDetailTrialView += DTORepaymentScheduleDetailTrialView(
                period = schedule.period,
                installment = schedule.instalment,
                principal = schedule.principal,
                interest = schedule.interest,
                repaymentDate = schedule.dueDate.toString(),
                fee = schedule.fee
            )
        }
        return DTORepaymentScheduleTrialView(
            installment = installment,
            fee = immediateFee,
            interestRate = interestRate,
            schedule = dtoRepaymentScheduleDetailTrialView
        )
    }
}