package cn.sunline.saas.runner

import cn.sunline.saas.channel.party.organisation.service.OrganisationService
import cn.sunline.saas.channel.rbac.services.UserService
import cn.sunline.saas.channel.statistics.services.CustomerDetailService
import cn.sunline.saas.dapr_wrapper.actor.model.ActorContext
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.loan.service.LoanApplyHandleService
import cn.sunline.saas.loan_apply.service.LoanApplyAppService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.rpc.bindings.impl.ChannelBindingsImpl
import cn.sunline.saas.scheduler.ActorType
import cn.sunline.saas.scheduler.create.CreateScheduler
import cn.sunline.saas.scheduler.dojob.*
import cn.sunline.saas.scheduler.job.helper.SchedulerJobHelper
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import cn.sunline.saas.workflow.defintion.services.ActivityDefinitionService
import cn.sunline.saas.workflow.defintion.services.EventDefinitionService
import cn.sunline.saas.workflow.defintion.services.ProcessDefinitionService
import cn.sunline.saas.workflow.event.handle.factory.EventFactory
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepDataService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class JobRunner(
    private val loanAgentService: LoanAgentService,
    private val loanApplyHandleService: LoanApplyHandleService,
    private val userService: UserService,
    private val tenantDateTime: TenantDateTime,
    private val loanApplyAppService: LoanApplyAppService,
    private val organisationService: OrganisationService,
    private val customerDetailService: CustomerDetailService,
    private val channelBindingsImpl: ChannelBindingsImpl,
    private val processDefinitionService: ProcessDefinitionService,
    private val processStepService: ProcessStepService,
    private val activityDefinitionService: ActivityDefinitionService,
    private val activityStepService: ActivityStepService,
    private val eventDefinitionService: EventDefinitionService,
    private val eventStepService: EventStepService,
    private val eventStepDataService: EventStepDataService,
    private val schedulerJobHelper: SchedulerJobHelper,
    private val createScheduler: CreateScheduler,
    private val eventFactory: EventFactory,
) {

    @PostConstruct
    fun run() {
        ActorContext.registerActor(ActorType.LOAN_APPLY_HANDLE.name,LoanApplyHandleSchedulerTask(loanAgentService, loanApplyHandleService, userService, schedulerJobHelper, tenantDateTime))
        ActorContext.registerActor(ActorType.LOAN_APPLY_SUBMIT.name,LoanApplySubmitSchedulerTask(tenantDateTime, schedulerJobHelper, loanAgentService))
        ActorContext.registerActor(ActorType.LOAN_APPLY_STATISTICS.name,LoanApplyStatisticsSchedulerTask(tenantDateTime,schedulerJobHelper,loanApplyAppService))
        ActorContext.registerActor(ActorType.BUSINESS_STATISTICS.name,BusinessStatisticsSchedulerTask(tenantDateTime, schedulerJobHelper, loanApplyAppService))
        ActorContext.registerActor(ActorType.CHANNEL_STATISTICS.name,ChannelStatisticsSchedulerTask(tenantDateTime, schedulerJobHelper, organisationService, customerDetailService))
        ActorContext.registerActor(ActorType.SYNC_CHANNEL.name,ChannelSyncSchedulerTask(tenantDateTime, schedulerJobHelper, organisationService, channelBindingsImpl))
        ActorContext.registerActor(ActorType.CREATE_EVENT.name,CreateEventSchedulerTask(processDefinitionService, processStepService, activityDefinitionService, activityStepService, eventDefinitionService, eventStepService, eventStepDataService, schedulerJobHelper, createScheduler))
        ActorContext.registerActor(ActorType.SET_EVENT_USER.name,SetEventUserSchedulerTask(userService, eventStepService, eventFactory, schedulerJobHelper, loanApplyHandleService))
        ActorContext.registerActor(ActorType.FINISH_EVENT_HANDLE.name,FinishEventHandleSchedulerTask(processStepService, activityStepService, eventStepService, tenantDateTime, schedulerJobHelper))
    }
}