package cn.sunline.saas.scheduler.dojob

import cn.sunline.saas.channel.rbac.modules.Position
import cn.sunline.saas.channel.rbac.modules.User
import cn.sunline.saas.channel.rbac.services.UserService
import cn.sunline.saas.dapr_wrapper.actor.ActorCommand
import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.dapr_wrapper.actor.payload
import cn.sunline.saas.loan.model.dto.DTOLoanApplyHandle
import cn.sunline.saas.loan.service.LoanApplyHandleService
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.job.helper.SchedulerJobHelper
import cn.sunline.saas.scheduler.dojob.dto.DTOSetEventUserScheduler
import cn.sunline.saas.workflow.event.handle.factory.EventFactory
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.services.EventStepService
import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

class SetEventUserSchedulerTask(
    private val userService: UserService,
    private val eventStepService: EventStepService,
    private val eventFactory: EventFactory,
    private val schedulerJobHelper: SchedulerJobHelper,
    private val loanApplyHandleService: LoanApplyHandleService,
    actorType:String = ActorType.SET_EVENT_USER.name,
    entityConfig: EntityConfig? = null
): AbstractActor(actorType, entityConfig) {

    private val threshold: BigDecimal = BigDecimal(10)

    private val logger = KotlinLogging.logger {  }

    data class DTOUserTask(
        val username:String,
        var number:Int,
        var percentage: BigDecimal
    )

    override fun doJob(actorId: String, jobId: String, data: ActorCommand) {
        val schedulerJobLog = schedulerJobHelper.execute(jobId)

        val payload = data.payload<DTOSetEventUserScheduler>()?: run {
            logger.error { "data error" }
            schedulerJobHelper.failed(schedulerJobLog,"data error")
            ActorReminderService.deleteReminders(actorType, actorId, jobId)
            return
        }
        val event = eventStepService.getOne(payload.eventStepId.toLong())?: run {
            logger.error { "event step id [${payload.eventStepId}] not found !!" }
            schedulerJobHelper.failed(schedulerJobLog,"event step id [${payload.eventStepId}] not found !!")
            ActorReminderService.deleteReminders(actorType, actorId, jobId)
            return
        }
        event.next?.run {
            val nextEvent = eventStepService.getOne(this)?: run {
                logger.error { "next event step id [${this}] not found !!" }
                schedulerJobHelper.failed(schedulerJobLog,"next event step id [${this}] not found !!")
                ActorReminderService.deleteReminders(actorType, actorId, jobId)
                return
            }

            if(nextEvent.user != null){
                logger.info { "user already exist !!" }
                schedulerJobHelper.succeed(schedulerJobLog)
                ActorReminderService.deleteReminders(actorType, actorId, jobId)
                return
            }
        }

        var user = event.user

        if(payload.isCurrentEventStep){
            user = getUser(payload.position)?: run {
                return
            }
        }
        event.next?: run {
            user = getUser(payload.position)?: run {
                return
            }
        }

        try {
            if(payload.isCurrentEventStep){
                loanApplyHandleService.saveOne(
                    DTOLoanApplyHandle(
                        applicationId = payload.applicationId,
                        supplement = user
                    )
                )
                eventFactory.instance(event.eventDefinition.type).setCurrent(user!!,event,payload.applicationId.toLong())
            } else {
                eventFactory.instance(event.eventDefinition.type).setNext(user!!,event,payload.applicationId.toLong())
            }

            schedulerJobHelper.succeed(schedulerJobLog)
            ActorReminderService.deleteReminders(actorType, actorId, jobId)

        } catch (e:Exception){
            schedulerJobHelper.failed(schedulerJobLog,e.localizedMessage)
            ActorReminderService.deleteReminders(actorType, actorId, jobId)
            return
        }
    }

    private fun getUser(positionId:String):String?{
        val users = getUsersByPosition(positionId)
        if(users.isEmpty()){
            logger.info("usersTask is empty")
            return null
        }

        val events = eventStepService.getPageWithTenant({ root, _, builder ->
            val predicates = mutableListOf<Predicate>()
            val userBuilder = builder.`in`(root.get<String>("user"))
            users.forEach {
                userBuilder.value(it.username)
            }
            predicates.add(userBuilder)
            predicates.add(builder.equal(root.get<StepStatus>("status"),StepStatus.PROCESSING))
            builder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged())

        val userTasks = mutableListOf<DTOUserTask>()

        users.forEach { user ->
            val number = events.count { it.user == user.username }
            userTasks.add(
                DTOUserTask(
                    user.username,
                    number,
                    BigDecimal(number).divide(threshold).setScale(2)
                )
            )
        }

        userTasks.sortBy { it.percentage }

        if(userTasks.first().percentage >= BigDecimal.ONE.setScale(2)){
            logger.info("users are full task")
            return null
        }

        return userTasks.first().username
    }

    private fun getUsersByPosition(positionId:String):List<User>{
        return userService.getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            val positionTable = root.join<User, Position>("position", JoinType.INNER)
            predicates.add(criteriaBuilder.equal(positionTable.get<String>("id"),positionId))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged()).content
    }
}

