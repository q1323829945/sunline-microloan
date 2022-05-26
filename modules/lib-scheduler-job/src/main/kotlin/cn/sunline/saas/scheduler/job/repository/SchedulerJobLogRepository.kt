package cn.sunline.saas.scheduler.job.repository

import cn.sunline.saas.base_jpa.repositories.BaseRepository
import cn.sunline.saas.scheduler.job.model.SchedulerJobLog

/**
 * @title: SchedulerJobLogRepository
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 11:05
 */
interface SchedulerJobLogRepository : BaseRepository<SchedulerJobLog, Long> {
}