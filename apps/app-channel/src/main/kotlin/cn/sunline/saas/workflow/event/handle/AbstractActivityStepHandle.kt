package cn.sunline.saas.workflow.event.handle

import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.db.ActivityStep
import cn.sunline.saas.workflow.step.modules.dto.DTOActivityStepChange
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService

abstract class AbstractActivityStepHandle(
    private val activityStepService: ActivityStepService,
    processStepService: ProcessStepService,
    private val tenantDateTime: TenantDateTime
):AbstractProcessStepHandle(processStepService, tenantDateTime) {

    protected fun getActivity(activityStepId:Long): ActivityStep?{
        return activityStepService.getOne(activityStepId)
    }

    protected fun setCurrentActivityStart(activityStepId: Long){
        val activity = activityStepService.updateOne(activityStepId,
            DTOActivityStepChange(
                status = StepStatus.PROCESSING,
                start = tenantDateTime.now().toDate()
            )
        )
        setProcessStart(activity.processStepId)
    }

    private fun getNextActivity(activityStep: ActivityStep): ActivityStep?{
        return activityStep.next?.run { activityStepService.getOne(this) }
    }

    protected fun setActivityFinishAndGetNextActivity(activityStepId:Long):ActivityStep?{
        val activity = getActivity(activityStepId)
        activity?.run {
            return setActivityFinish(this.id)
        }
        return null
    }

    protected fun setActivityFinish(activityStepId: Long):ActivityStep?{
        val activityStep = activityStepService.updateOne(
            activityStepId,
            DTOActivityStepChange(status = StepStatus.PASSED, end = tenantDateTime.now().toDate())
        )
        return if(activityStep.next == null){
            setProcessFinish(activityStep.processStepId)
            null
        }else {
            setNextActivityStart(activityStep)
        }
    }

    private fun setNextActivityStart(activityStep: ActivityStep):ActivityStep?{
        val nextActivity = getNextActivity(activityStep)
        nextActivity?.run {
            activityStepService.updateOne(this.id,
                DTOActivityStepChange(status = StepStatus.PROCESSING, start = tenantDateTime.now().toDate())
            )
            return this
        }
        return null
    }
}