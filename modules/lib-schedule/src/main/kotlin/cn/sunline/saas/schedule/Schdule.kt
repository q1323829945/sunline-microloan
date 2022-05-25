package cn.sunline.saas.formula

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
    val instalment: BigDecimal?,
    val principal: BigDecimal,
    val interest: BigDecimal,
    val remainingPrincipal: BigDecimal
)
