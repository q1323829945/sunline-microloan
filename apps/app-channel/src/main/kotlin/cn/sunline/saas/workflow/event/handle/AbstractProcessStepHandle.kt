package cn.sunline.saas.workflow.event.handle

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.db.ActivityStep
import cn.sunline.saas.workflow.step.modules.db.EventStep
import cn.sunline.saas.workflow.step.modules.db.ProcessStep
import cn.sunline.saas.workflow.step.modules.dto.DTOProcessStepChange
import cn.sunline.saas.workflow.step.services.ProcessStepService
import org.springframework.data.domain.Pageable
import javax.persistence.criteria.Predicate

abstract class AbstractProcessStepHandle(
    private val processStepService: ProcessStepService,
    private val tenantDateTime: TenantDateTime
) {

    protected fun setProcessFinish(processStepId:Long){
        val process = processStepService.getOne(processStepId)
        process?.run {
            processStepService.updateOne(
                this.id,
                DTOProcessStepChange(
                    status = StepStatus.PASSED,
                    end = tenantDateTime.now().toDate()
                )
            )
        }
    }

    protected fun setProcessProcessing(processStepId: Long){
        processStepService.updateOne(
            processStepId,
            DTOProcessStepChange(
                status = StepStatus.PROCESSING
            )
        )
    }

    protected fun getProcess(eventStep: EventStep): ProcessStep?{
        return processStepService.getPageWithTenant({ root, _, builder ->
            val predicates = mutableListOf<Predicate>()
            val activity = root.join<ProcessStep, ActivityStep>("activities")
            val event = activity.join<ActivityStep, ProcessStep>("events")
            predicates.add(builder.equal(event.get<Long>("id"),eventStep.id))
            builder.and(*(predicates.toTypedArray()))
        }, Pageable.unpaged()).firstOrNull()
    }
}