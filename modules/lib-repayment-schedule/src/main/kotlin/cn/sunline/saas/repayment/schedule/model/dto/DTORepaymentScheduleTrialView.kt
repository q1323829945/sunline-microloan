package cn.sunline.saas.repayment.schedule.model.dto

import org.joda.time.Instant
import java.math.BigDecimal

data class DTORepaymentScheduleView(
    val repaymentScheduleId: Long? = null,
    val installment: BigDecimal? = null,
    val interestRate: BigDecimal,
    var schedule: MutableList<DTORepaymentScheduleDetailView>
)

data class DTORepaymentScheduleDetailView(
    val id: Long? = null,
    val repaymentScheduleId: Long? = null,
    val period: Int,
    val repaymentDate: Instant,
    val installment: BigDecimal,
    val principal: BigDecimal,
    val interest: BigDecimal,
    val remainPrincipal: BigDecimal
)
