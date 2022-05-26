package cn.sunline.saas.scheduler.job.service

import cn.sunline.saas.base_jpa.services.BaseRepoService
import cn.sunline.saas.scheduler.job.model.SchedulerTimer
import cn.sunline.saas.scheduler.job.repository.SchedulerTimerRepository
import org.springframework.stereotype.Service

/**
 * @title: SchedulerTimerService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 11:10
 */
@Service
class SchedulerTimerService(private val schedulerTimerRepository: SchedulerTimerRepository) :
    BaseRepoService<SchedulerTimer, Long>(schedulerTimerRepository) {
}