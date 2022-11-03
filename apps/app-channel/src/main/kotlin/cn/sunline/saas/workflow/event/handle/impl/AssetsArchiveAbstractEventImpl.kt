package cn.sunline.saas.workflow.event.handle.impl

import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.loan.exception.LoanApplyNotFoundException
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.loan.service.LoanApplyService
import cn.sunline.saas.modules.dto.DTOLoanApplyAuditAdd
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.create.CreateScheduler
import cn.sunline.saas.services.LoanApplyAuditService
import cn.sunline.saas.workflow.event.handle.AbstractEventHandle
import cn.sunline.saas.workflow.event.handle.dto.DTOArchive
import cn.sunline.saas.workflow.event.handle.helper.EventHandleCommand
import cn.sunline.saas.workflow.event.handle.helper.payload
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepChange
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepDataService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService

class AssetsArchiveAbstractEventImpl(
    private val eventStepService: EventStepService,
    eventStepDataService: EventStepDataService,
    activityStepService: ActivityStepService,
    processStepService: ProcessStepService,
    private val tenantDateTime: TenantDateTime,
    createScheduler: CreateScheduler,
    private val loanApplyService: LoanApplyService,
    private val loanAgentService: LoanAgentService,
    private val loanApplyAuditService: LoanApplyAuditService,
): AbstractEventHandle(eventStepService,eventStepDataService,activityStepService,processStepService,tenantDateTime,createScheduler) {

    override fun doHandle(eventHandleCommand: EventHandleCommand){
        if(eventHandleCommand.status == StepStatus.REJECTED){
            rejected(eventHandleCommand.eventStep,eventHandleCommand.applicationId)
            return
        }

        val archive = eventHandleCommand.payload<DTOArchive>()!!
        val productType = archive.productType
        val data = archive.data

        val loanApply = when (productType) {
            ProductType.NEW_CLIENT -> loanApplyService.updateNewClientLoan(objectMapper.writeValueAsString(data))
            ProductType.CLIENT -> loanApplyService.updateClientLoan(objectMapper.writeValueAsString(data))
            ProductType.TEACHER -> loanApplyService.updateTeacherLoan(objectMapper.writeValueAsString(data))
            ProductType.KABUHAYAN -> loanApplyService.updateKabuhayanLoan(objectMapper.writeValueAsString(data))
            ProductType.CORPORATE -> loanApplyService.updateCorporateLoan(objectMapper.writeValueAsString(data))
        }

        audit(loanApply.applicationId)

        passed(eventHandleCommand.eventStep,eventHandleCommand.applicationId,loanApply.data)

        handleNext(eventHandleCommand.user,eventHandleCommand.eventStep,eventHandleCommand.applicationId)
    }

    private fun audit(applicationId: Long) {
        val loanAgent = loanAgentService.getOne(applicationId) ?: throw LoanApplyNotFoundException("Invalid loan apply")
        loanApplyAuditService.addLoanApplyAudit(
            dtoLoanApplyAuditAdd = DTOLoanApplyAuditAdd(
                applicationId = loanAgent.applicationId.toString(),
                name = loanAgent.name,
                productId = loanAgent.productId,
                term = loanAgent.loanApply?.term,
                amount = loanAgent.loanApply?.amount,
                data = loanAgent.data,
                status = loanAgent.status
            )
        )
    }
}