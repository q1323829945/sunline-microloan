package cn.sunline.saas.scheduler.job.exception

import cn.sunline.saas.exceptions.BusinessException
import cn.sunline.saas.exceptions.ManagementExceptionCode

/**
 * @title: SchedulerTimerNotConfigureException
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/25 13:52
 */
class SchedulerTimerNotConfigureException(
    exceptionMessage: String? = null,
    statusCode: ManagementExceptionCode = ManagementExceptionCode.SCHEDULER_TIMER_NOT_CONFIGURE
) : BusinessException(exceptionMessage, statusCode) {
}