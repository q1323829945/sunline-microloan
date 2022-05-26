package cn.sunline.saas.scheduler.job.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.scheduler.job.model.SchedulerTimer

/**
 * @title: SchedulerTimerRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 11:07
 */
interface SchedulerTimerRepository : BaseRepository<SchedulerTimer, Long> {
}