package cn.sunline.saas.scheduler.dojob

import cn.sunline.saas.dapr_wrapper.actor.ActorReminderService
import cn.sunline.saas.dapr_wrapper.actor.model.AbstractActor
import cn.sunline.saas.dapr_wrapper.actor.model.EntityConfig
import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.global.constant.PositionType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.loan.model.db.LoanAgent
import cn.sunline.saas.loan.model.db.LoanApplyHandle
import cn.sunline.saas.loan.model.dto.DTOLoanApplyHandle
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.loan.service.LoanApplyHandleService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.channel.rbac.modules.Position
import cn.sunline.saas.channel.rbac.modules.User
import cn.sunline.saas.channel.rbac.services.UserService
import cn.sunline.saas.dapr_wrapper.actor.ActorCommand
import cn.sunline.saas.global.util.getTenant
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.job.component.execute
import cn.sunline.saas.scheduler.job.component.succeed
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.math.BigDecimal
import javax.persistence.criteria.JoinType
import javax.persistence.criteria.Predicate

@Service
class LoanApplyHandleSchedulerTask (
    actorType:String = ActorType.LOAN_APPLY_HANDLE.name,
    entityConfig: EntityConfig? = null
): AbstractActor(actorType, entityConfig) {
    private var logger = KotlinLogging.logger {}

    val threshold: BigDecimal = BigDecimal(5)

    @Autowired
    private lateinit var loanAgentService: LoanAgentService

    @Autowired
    private lateinit var loanApplyHandleService: LoanApplyHandleService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var schedulerJobLogService: SchedulerJobLogService

    @Autowired
    private lateinit var tenantDateTime: TenantDateTime


    data class DTOUserTask(
        val username:String,
        var number:Int,
        var percentage:BigDecimal
    )

    override fun doJob(applicationId: String, jobId: String, data: ActorCommand) {

        val schedulerJobLog = schedulerJobLogService.getOne(jobId.toLong())
        schedulerJobLog?.run {
            ContextUtil.setTenant(this.getTenantId().toString())
            this.execute(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }

        val loanApplyHandle = loanApplyHandleService.getOne(applicationId.toLong())

        if(loanApplyHandle != null){
            logger.info("The loan apply has been manually assigned!")
            schedulerJobLog?.run {
                ContextUtil.setTenant(this.getTenantId().toString())
                this.succeed(tenantDateTime.now())
                schedulerJobLogService.save(this)
            }
            ActorReminderService.deleteReminders(actorType, applicationId, jobId)
            return
        }

        val userTasks = getUsersTaskCount()

        if(userTasks.isEmpty()){
            return
        }

        loanApplyHandleService.saveOne(
            DTOLoanApplyHandle(
                applicationId = applicationId,
                supplement = userTasks.first().username
            )
        )

        schedulerJobLog?.run {
            this.succeed(tenantDateTime.now())
            schedulerJobLogService.save(this)
        }

        ActorReminderService.deleteReminders(actorType, applicationId, jobId)
    }

    private fun getUsersTaskCount():MutableList<DTOUserTask>{
        val users = getUsersByPosition(PositionType.SUPPLEMENT)

        if(users.isEmpty()){
            logger.info("usersTask is empty")
            return mutableListOf()
        }

        val loanApply = loanAgentService.getPageWithTenant({ root, _, criteriaBuilder ->
            val loanApplyHandleTable = root.join<LoanAgent, LoanApplyHandle>("handle", JoinType.INNER)
            val predicates = mutableListOf<Predicate>()
            val supplementCriteriaBuilder = criteriaBuilder.`in`(loanApplyHandleTable.get<String>("supplement"))
            users.forEach {
                supplementCriteriaBuilder.value(it.username)
            }
            predicates.add(supplementCriteriaBuilder)
            predicates.add(criteriaBuilder.equal(root.get<ApplyStatus>("status"), ApplyStatus.RECORD))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        },Pageable.unpaged())

        val userTasks = mutableListOf<DTOUserTask>()

        users.forEach { user ->
            val number = loanApply.count { it.handle?.supplement == user.username }
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
            return mutableListOf()
        }

        return userTasks
    }

    private fun getUsersByPosition(positionType: PositionType):List<User>{
        return userService.getPageWithTenant({ root, _, criteriaBuilder ->
            val predicates = mutableListOf<Predicate>()
            val positionTable = root.join<User,Position>("position",JoinType.INNER)
            predicates.add(criteriaBuilder.equal(positionTable.get<String>("id"),positionType.position + ContextUtil.getTenant()))
            criteriaBuilder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged()).content
    }
}