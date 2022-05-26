package cn.sunline.saas.scheduler.job.service

import cn.sunline.saas.multi_tenant.services.BaseMultiTenantRepoService
import cn.sunline.saas.scheduler.job.model.SchedulerJobLog
import cn.sunline.saas.scheduler.job.repository.SchedulerJobLogRepository
import org.springframework.stereotype.Service

/**
 * @title: SchedulerJobLogService
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 11:08
 */
@Service
class SchedulerJobLogService(private val schedulerJobLogRepository: SchedulerJobLogRepository) :
    BaseMultiTenantRepoService<SchedulerJobLog, Long>(schedulerJobLogRepository) {
}