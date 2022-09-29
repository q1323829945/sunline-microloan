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
import cn.sunline.saas.scheduler.dojob.*
import cn.sunline.saas.scheduler.job.service.SchedulerJobLogService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class JobRunner(
    private val loanAgentService: LoanAgentService,
    private val loanApplyHandleService: LoanApplyHandleService,
    private val userService: UserService,
    private val schedulerJobLogService: SchedulerJobLogService,
    private val tenantDateTime: TenantDateTime,
    private val loanApplyAppService: LoanApplyAppService,
    private val organisationService: OrganisationService,
    private val customerDetailService: CustomerDetailService,
    private val channelBindingsImpl: ChannelBindingsImpl,
) {

    @PostConstruct
    fun run() {
        ActorContext.registerActor(ActorType.LOAN_APPLY_HANDLE.name,LoanApplyHandleSchedulerTask(loanAgentService, loanApplyHandleService, userService, schedulerJobLogService, tenantDateTime))
        ActorContext.registerActor(ActorType.LOAN_APPLY_SUBMIT.name,LoanApplySubmitSchedulerTask(tenantDateTime, schedulerJobLogService, loanAgentService))
        ActorContext.registerActor(ActorType.LOAN_APPLY_STATISTICS.name,LoanApplyStatisticsSchedulerTask(tenantDateTime,schedulerJobLogService,loanApplyAppService))
        ActorContext.registerActor(ActorType.BUSINESS_STATISTICS.name,BusinessStatisticsSchedulerTask(tenantDateTime, schedulerJobLogService, loanApplyAppService))
        ActorContext.registerActor(ActorType.CHANNEL_STATISTICS.name,ChannelStatisticsSchedulerTask(tenantDateTime, schedulerJobLogService, organisationService, customerDetailService))
        ActorContext.registerActor(ActorType.SYNC_CHANNEL.name,ChannelSyncSchedulerTask(tenantDateTime, schedulerJobLogService, organisationService, channelBindingsImpl))
    }
}