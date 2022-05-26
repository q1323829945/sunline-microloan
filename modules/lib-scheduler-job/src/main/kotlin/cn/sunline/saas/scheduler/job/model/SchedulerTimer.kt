package cn.sunline.saas.scheduler.job.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotNull

/**
 * @title: SchedulerTimer
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 9:55
 */
@Entity
@Table(name = "scheduler_timer")
class SchedulerTimer(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "switch_day_time", nullable = false, columnDefinition = "tinyint not null")
    var switchDayTime: Int = 24
)