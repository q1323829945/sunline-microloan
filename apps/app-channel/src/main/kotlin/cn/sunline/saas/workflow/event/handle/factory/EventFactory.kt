package cn.sunline.saas.workflow.event.handle.factory

import cn.sunline.saas.channel.product.service.ProductService
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.loan.service.LoanApplyHandleService
import cn.sunline.saas.loan.service.LoanApplyService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.create.CreateScheduler
import cn.sunline.saas.services.LoanApplyAuditService
import cn.sunline.saas.workflow.defintion.modules.EventType
import cn.sunline.saas.workflow.defintion.modules.EventType.*
import cn.sunline.saas.workflow.event.handle.AbstractEventHandle
import cn.sunline.saas.workflow.event.handle.impl.*
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepDataService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService
import org.springframework.stereotype.Component

@Component
class EventFactory(
    private val eventStepService: EventStepService,
    private val eventStepDataService: EventStepDataService,
    private val activityStepService: ActivityStepService,
    private val processStepService: ProcessStepService,
    private val tenantDateTime: TenantDateTime,
    private val createScheduler: CreateScheduler,
    private val productService: ProductService,
    private val loanApplyService: LoanApplyService,
    private val loanAgentService: LoanAgentService,
    private val loanApplyAuditService: LoanApplyAuditService,
    private val loanApplyHandleService: LoanApplyHandleService,
) {

    fun instance(eventType: EventType): AbstractEventHandle {
        return when(eventType){
            CHECK_CUSTOMER -> CheckCustomerAbstractEventImpl(eventStepService,eventStepDataService, activityStepService, processStepService, tenantDateTime, createScheduler,loanAgentService)
            CHECK_DATA -> CheckDataAbstractEventImpl(eventStepService,eventStepDataService, activityStepService, processStepService, tenantDateTime, createScheduler,loanAgentService)
            RECOMMEND_PRODUCT -> RecommendProductAbstractEventImpl(eventStepService,eventStepDataService, activityStepService, processStepService, tenantDateTime, createScheduler, productService, loanApplyService, loanAgentService, loanApplyAuditService)
            COLLECT_INFORMATION -> CollectInformationAbstractEventImpl(eventStepService,eventStepDataService, activityStepService, processStepService, tenantDateTime, createScheduler,loanApplyService)
            CUSTOMER_ARCHIVE -> CustomerArchiveAbstractEventImpl(eventStepService,eventStepDataService, activityStepService, processStepService, tenantDateTime, createScheduler,loanApplyService, loanAgentService, loanApplyAuditService)
            ASSETS_ARCHIVE -> AssetsArchiveAbstractEventImpl(eventStepService,eventStepDataService, activityStepService, processStepService, tenantDateTime, createScheduler,loanApplyService, loanAgentService, loanApplyAuditService)
            PRE_APPROVAL -> PreApprovalAbstractEventImpl(eventStepService,eventStepDataService, activityStepService, processStepService, tenantDateTime, createScheduler,loanApplyService, loanAgentService, loanApplyHandleService)
        }
    }
}