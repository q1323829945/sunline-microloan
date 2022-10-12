package cn.sunline.saas.scheduler.job.component

import cn.sunline.saas.scheduler.job.model.JobStatus
import cn.sunline.saas.scheduler.job.model.SchedulerJobLog
import org.joda.time.DateTime

/**
 * @title: SchedulerJobLogComponent
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/5/30 16:54
 */
fun SchedulerJobLog.execute(date: DateTime, context: String? = null) {
    this.executeContext = context
    this.executeDate = date.toDate()
    this.jobStatus = JobStatus.RUNNING
}


fun SchedulerJobLog.succeed(date: DateTime) {
    this.completedDate = date.toDate()
    this.jobStatus = JobStatus.SUCCEED
}

fun SchedulerJobLog.failed(date: DateTime, context: String? = null) {
    this.completedDate = date.toDate()
    this.errorContext = context
    this.jobStatus = JobStatus.FAILED
}