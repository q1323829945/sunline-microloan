package cn.sunline.saas.workflow.event.handle.impl

import cn.sunline.saas.channel.product.service.ProductService
import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.loan.exception.LoanApplyNotFoundException
import cn.sunline.saas.loan.model.dto.DTOQuestionnaire
import cn.sunline.saas.loan.service.LoanAgentService
import cn.sunline.saas.loan.service.LoanApplyService
import cn.sunline.saas.loan.service.assembly.LoanApplyAssembly
import cn.sunline.saas.modules.dto.DTOLoanApplyAuditAdd
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.create.CreateScheduler
import cn.sunline.saas.services.LoanApplyAuditService
import cn.sunline.saas.workflow.event.handle.AbstractEventHandle
import cn.sunline.saas.workflow.event.handle.dto.DTORecommendProduct
import cn.sunline.saas.workflow.event.handle.helper.EventHandleCommand
import cn.sunline.saas.workflow.event.handle.helper.payload
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.dto.DTOEventStepChange
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepDataService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService
import com.fasterxml.jackson.module.kotlin.convertValue

class RecommendProductAbstractEventImpl(
    private val eventStepService: EventStepService,
    eventStepDataService: EventStepDataService,
    activityStepService: ActivityStepService,
    processStepService: ProcessStepService,
    private val tenantDateTime: TenantDateTime,
    createScheduler: CreateScheduler,
    private val productService: ProductService,
    private val loanApplyService: LoanApplyService,
    private val loanAgentService: LoanAgentService,
    private val loanApplyAuditService: LoanApplyAuditService,
): AbstractEventHandle(eventStepService,eventStepDataService,activityStepService,processStepService,tenantDateTime,createScheduler) {

    override fun doHandle(eventHandleCommand: EventHandleCommand){
        val data = eventHandleCommand.payload<DTORecommendProduct>()!!

        if(eventHandleCommand.status == StepStatus.REJECTED){
            rejected(eventHandleCommand.eventStep)
            setEventStepData(eventHandleCommand.eventStep.id,eventHandleCommand.applicationId)
            return
        }

        val loanAgent = loanAgentService.updateOne(data.applicationId, data.productId)
        val product = productService.getProduct(data.productId)

        val dtoLoanAgent = LoanApplyAssembly.convertToLoanAgent(loanAgent.data)
        dtoLoanAgent.applicationId = loanAgent.applicationId.toString()

        val questionnaires = product.questionnaires?.run { objectMapper.convertValue<List<DTOQuestionnaire>>(this) }
        val loanApply = when (product.productType) {
            ProductType.NEW_CLIENT -> loanApplyService.addNewClientLoan(
                LoanApplyAssembly.convertToNewClientLoan(
                    dtoLoanAgent,
                    questionnaires
                )
            )

            ProductType.CLIENT -> loanApplyService.addClientLoan(
                LoanApplyAssembly.convertToClientLoan(
                    dtoLoanAgent,
                    questionnaires
                )
            )

            ProductType.TEACHER -> loanApplyService.addTeacherLoan(LoanApplyAssembly.convertToTeacherLoan(dtoLoanAgent))
            ProductType.KABUHAYAN -> loanApplyService.addKabuhayanLoan(
                LoanApplyAssembly.convertToKabuhayanLoan(
                    dtoLoanAgent,
                    questionnaires
                )
            )

            ProductType.CORPORATE -> loanApplyService.addCorporateLoan(LoanApplyAssembly.convertToCorporateLoan(dtoLoanAgent))
        }

        audit(loanApply.applicationId)

        val loanAgentData = LoanApplyAssembly.convertToLoanAgent(loanAgent.data)
        loanAgentData.applicationId = loanAgent.applicationId.toString()
        loanAgentData.productType = product.productType
        loanAgentData.productId = product.id
        loanAgentData.productName = product.name

        eventStepService.updateOne(
            eventHandleCommand.eventStep.id,
            DTOEventStepChange(
                status = eventHandleCommand.status,
                end = tenantDateTime.now().toDate(),
            )
        )
        setEventStepData(eventHandleCommand.eventStep.id,eventHandleCommand.applicationId,objectMapper.writeValueAsString(loanAgentData))

        handleNext(eventHandleCommand.user,eventHandleCommand.eventStep,eventHandleCommand.applicationId,loanApply.data)
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