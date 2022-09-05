package cn.sunline.saas.runner

import cn.sunline.saas.dapr_wrapper.actor.model.ActorContext
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.dojob.LoanApplyHandleSchedulerTask
import cn.sunline.saas.scheduler.dojob.LoanApplySubmitSchedulerTask
import cn.sunline.saas.scheduler.dojob.LoanApplyStatisticsSchedulerTask
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class JobRunner {
    @Autowired
    private lateinit var actorContext: ActorContext

    @Autowired
    private lateinit var loanApplySubmitSchedulerTask: LoanApplySubmitSchedulerTask

    @Autowired
    private lateinit var loanApplyStatisticsSchedulerTask: LoanApplyStatisticsSchedulerTask

    @Autowired
    private lateinit var loanApplyHandleSchedulerTask: LoanApplyHandleSchedulerTask


    @PostConstruct
    fun run() {
        actorContext.registerActor(ActorType.LOAN_APPLY_SUBMIT.name,loanApplySubmitSchedulerTask)
        actorContext.registerActor(ActorType.LOAN_APPLY_STATISTICS.name,loanApplyStatisticsSchedulerTask)
        actorContext.registerActor(ActorType.LOAN_APPLY_HANDLE.name,loanApplyHandleSchedulerTask)
    }
}