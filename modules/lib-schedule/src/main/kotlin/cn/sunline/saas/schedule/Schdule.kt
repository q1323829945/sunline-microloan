package cn.sunline.saas.schedule

import org.joda.time.DateTime
import java.math.BigDecimal

/**
 * @title: Schdule
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/20 11:13
 */
data class Schedule(
    val fromDate: DateTime,
    val dueDate: DateTime,
    val installment: BigDecimal,
    val principal: BigDecimal,
    val interest: BigDecimal,
    val remainingPrincipal: BigDecimal,
    val period: Int,
    val interestRate: BigDecimal
)
