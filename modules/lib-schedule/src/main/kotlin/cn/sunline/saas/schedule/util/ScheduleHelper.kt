package cn.sunline.saas.schedule.util

import cn.sunline.saas.schedule.Schedule
import java.math.BigDecimal


object ScheduleHelper {

    data class DTORepaymentScheduleTrialView(
        val installment: String? = null,
        val fee: String,
        val interestRate: String,
        val schedule: MutableList<DTORepaymentScheduleDetailTrialView>
    )

    data class DTORepaymentScheduleDetailTrialView(
        val period: String,
        val repaymentDate: String,
        val installment: String,
        val principal: String,
        val interest: String,
        val fee: String
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
                period = schedule.period.toString(),
                installment = schedule.instalment.toPlainString(),
                principal = schedule.principal.toPlainString(),
                interest = schedule.interest.toPlainString(),
                repaymentDate = schedule.dueDate.toString(),
                fee = schedule.fee.toPlainString()
            )
        }
        return DTORepaymentScheduleTrialView(
            installment = installment.toPlainString(),
            fee = immediateFee.toPlainString(),
            interestRate = interestRate.toPlainString(),
            schedule = dtoRepaymentScheduleDetailTrialView
        )
    }
}