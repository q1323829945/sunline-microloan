package cn.sunline.saas.schedule

import org.joda.time.Instant
import java.math.BigDecimal

/**
 * @title: Schdule
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/20 11:13
 */
data class Schedule(
    val fromDate:Instant,
    val dueDate: Instant,
    val installment: BigDecimal,
    val principal: BigDecimal,
    val interest: BigDecimal,
    val remainingPrincipal: BigDecimal,
    val period: Int,
    val interestRate: BigDecimal
)
