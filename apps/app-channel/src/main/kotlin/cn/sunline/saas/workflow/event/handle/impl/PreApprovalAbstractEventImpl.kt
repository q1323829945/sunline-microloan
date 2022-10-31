package cn.sunline.saas.workflow.event.handle.impl

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.loan.model.dto.DTOLoanApplyHandle
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.loan.service.LoanApplyHandleService
import cn.sunline.saas.loan.service.LoanApplyService
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.create.CreateScheduler
import cn.sunline.saas.workflow.event.handle.AbstractEventHandle
import cn.sunline.saas.workflow.event.handle.dto.DTOPreApproval
import cn.sunline.saas.workflow.event.handle.helper.EventHandleCommand
import cn.sunline.saas.workflow.event.handle.helper.payload
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepChange
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepDataService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService

class PreApprovalAbstractEventImpl(
    private val eventStepService: EventStepService,
    private val eventStepDataService: EventStepDataService,
    private val activityStepService: ActivityStepService,
    private val processStepService: ProcessStepService,
    private val tenantDateTime: TenantDateTime,
    private val createScheduler: CreateScheduler,
    private val loanApplyService: LoanApplyService,
    private val loanAgentService: LoanAgentService,
    private val loanApplyHandleService: LoanApplyHandleService,
): AbstractEventHandle(eventStepService,eventStepDataService,activityStepService,processStepService,tenantDateTime,createScheduler) {

    override fun doHandle(eventHandleCommand: EventHandleCommand){
        val data = eventHandleCommand.payload<DTOPreApproval>()!!


        if(eventHandleCommand.status == StepStatus.REJECTED){
            rejected(eventHandleCommand.eventStep)
            setEventStepData(eventHandleCommand.eventStep.id,eventHandleCommand.applicationId,eventHandleCommand.data)
            return
        }


        loanApplyHandleService.saveOne(
            DTOLoanApplyHandle(
                applicationId = data.applicationId.toString(),
                supplementDate = tenantDateTime.now().toDate()
            )
        )
        loanAgentService.updateStatus(data.applicationId, ApplyStatus.SUBMIT)


        val loanApply = loanApplyService.getLoanApplyDetails(data.applicationId)

        eventStepService.updateOne(
            eventHandleCommand.eventStep.id,
            DTOEventStepChange(
                status = eventHandleCommand.status,
                end = tenantDateTime.now().toDate(),
            )
        )
        setEventStepData(eventHandleCommand.eventStep.id,eventHandleCommand.applicationId,loanApply)

        handleNext(eventHandleCommand.user,eventHandleCommand.eventStep,eventHandleCommand.applicationId,loanApply)
    }

}