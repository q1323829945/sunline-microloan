package cn.sunline.saas.workflow.event.handle.impl

import cn.sunline.saas.loan.exception.LoanApplyNotFoundException
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.create.CreateScheduler
import cn.sunline.saas.workflow.event.handle.AbstractEventHandle
import cn.sunline.saas.workflow.event.handle.helper.EventHandleCommand
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepChange
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepDataService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService

class CheckCustomerAbstractEventImpl(
    private val eventStepService: EventStepService,
    eventStepDataService: EventStepDataService,
    activityStepService: ActivityStepService,
    processStepService: ProcessStepService,
    private val tenantDateTime: TenantDateTime,
    createScheduler: CreateScheduler,
    private val loanAgentService: LoanAgentService,
): AbstractEventHandle(eventStepService,eventStepDataService,activityStepService,processStepService,tenantDateTime,createScheduler) {

    override fun doHandle(eventHandleCommand: EventHandleCommand){
        if(eventHandleCommand.status == StepStatus.REJECTED){
            rejected(eventHandleCommand.eventStep,eventHandleCommand.applicationId)
            return
        }
        val loanAgent = loanAgentService.getOne(eventHandleCommand.applicationId)?:throw LoanApplyNotFoundException("Invalid loan !!")

        passed(eventHandleCommand.eventStep,eventHandleCommand.applicationId,loanAgent.data)

        handleNext(eventHandleCommand.user,eventHandleCommand.eventStep,eventHandleCommand.applicationId)
    }
}