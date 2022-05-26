package cn.sunline.saas.scheduler.job.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.scheduler.job.model.SchedulerTask
import cn.sunline.saas.scheduler.job.repository.SchedulerTaskRepository
import org.springframework.stereotype.Service

/**
 * @title: SchedulerTaskService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 11:09
 */
@Service
class SchedulerTaskService(private val schedulerTaskRepository: SchedulerTaskRepository) :
    BaseMultiTenantRepoService<SchedulerTask, Long>(schedulerTaskRepository) {
}