package cn.sunline.saas.scheduler.create

import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.request.Timer
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.job.component.CalculateSchedulerTimer
import cn.sunline.saas.scheduler.job.model.SchedulerJobLog
import cn.sunline.saas.scheduler.job.model.SchedulerType
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import cn.sunline.saas.seq.Sequence
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CreateScheduler {

    private val interval: Int = 2

    @Autowired
    private lateinit var schedulerJobLogService: SchedulerJobLogService

    @Autowired
    private lateinit var calculateSchedulerTimer: CalculateSchedulerTimer

    @Autowired
    private lateinit var seq: Sequence

    fun create(actorType: ActorType, applicationId:String) {
        val accountDate = calculateSchedulerTimer.baseDateTime()
        val targetDateTime = accountDate.plusMinutes(interval)

        val jobId = seq.nextId()
        schedulerJobLogService.save(
            SchedulerJobLog(
                jobId,
                SchedulerType.REMINDER,
                targetDateTime.toDate(),
                applicationId,
                actorType.name,
                jobId.toString(),
                accountDate.toDate()
            )
        )

        val dueTime = actorType.dueTime

        val periodTime = actorType.periodTime

        ActorReminderService.createReminders(actorType.name, applicationId, jobId.toString(), dueTime, periodTime)
    }

}