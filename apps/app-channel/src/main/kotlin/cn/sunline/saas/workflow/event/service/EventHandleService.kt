package cn.sunline.saas.workflow.event.service

import cn.sunline.saas.global.constant.ProductType
import cn.sunline.saas.loan.service.LoanApplyService
import cn.sunline.saas.loan.service.assembly.LoanApplyAssembly
import cn.sunline.saas.multi_tenant.util.TenantDateTime
import cn.sunline.saas.scheduler.create.CreateScheduler
import cn.sunline.saas.workflow.defintion.modules.EventType
import cn.sunline.saas.workflow.event.handle.factory.EventFactory
import cn.sunline.saas.workflow.event.handle.helper.EventHandleCommand
import cn.sunline.saas.workflow.event.service.dto.DTOEventHandle
import cn.sunline.saas.workflow.event.service.dto.DTOEventHandleDetail
import cn.sunline.saas.workflow.event.service.dto.DTOEventHandleView
import cn.sunline.saas.workflow.step.exception.ActivityStepNotFoundException
import cn.sunline.saas.workflow.step.exception.EventStepNotFoundException
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.db.ActivityStep
import cn.sunline.saas.workflow.step.modules.db.ProcessStep
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import javax.persistence.criteria.Predicate

@Service
class EventHandleService(
    private val tenantDateTime: TenantDateTime,
    private val eventFactory: EventFactory) {
    @Autowired
    private lateinit var processStepService: ProcessStepService
    @Autowired
    private lateinit var eventStepService: EventStepService
    @Autowired
    private lateinit var activityStepService: ActivityStepService
    @Autowired
    private lateinit var loanApplyService:LoanApplyService



    fun getEventHandlePaged(user:String? = null,status: StepStatus? = null,pageable: Pageable):Page<DTOEventHandleView>{
        val processes = processStepService.getPageWithTenant({ root, query, builder ->
            val predicates = mutableListOf<Predicate>()
            val activity = root.join<ProcessStep,ActivityStep>("activities")
            val event = activity.join<ActivityStep,ProcessStep>("events")
            user?.run { predicates.add(builder.equal(event.get<String>("user"),this)) }
            status?.run { predicates.add(builder.equal(event.get<StepStatus>("status"),this)) }
            query.distinct(true).where(builder.and(*(predicates.toTypedArray()))).restriction

        }, Pageable.unpaged())
        val dtoEventHandles = mutableListOf<DTOEventHandleView>()

        processes.forEach { process ->
            process.activities.forEach { activity ->
                activity.events.filter { user == null || it.user == user }
                    .filter { status == null || it.status == status }.forEach {
                        val handle = DTOEventHandleView(
                            id = it.id.toString(),
                            processName = process.processDefinition.name,
                            activityName = activity.activityDefinition.name,
                            eventType = it.eventDefinition.type,
                            position = activity.activityDefinition.position,
                            user = it.user,
                            status = it.status,
                            start = it.start?.run { tenantDateTime.toTenantDateTime(this).toDate() },
                            end = it.end?.run { tenantDateTime.toTenantDateTime(this).toDate() },
                        )
                        dtoEventHandles.add(handle)
                }
            }
        }

        dtoEventHandles.sortByDescending { it.start }

        return rePaged(dtoEventHandles,pageable)
    }


    fun updateEventStep(id:Long,dtoEventHandle: DTOEventHandle){
        val event = eventStepService.getOne(id)?: throw EventStepNotFoundException("Invalid event !!")
        eventFactory.instance(event.eventDefinition.type).doHandle(EventHandleCommand(dtoEventHandle.applicationId,event,dtoEventHandle.status,dtoEventHandle.user,dtoEventHandle.data))
    }

    fun detail(id: Long): DTOEventHandleDetail {
        val event = eventStepService.getOne(id)?: throw EventStepNotFoundException("Invalid event !!")
        val activity = activityStepService.getOne(event.activityStepId)?: throw ActivityStepNotFoundException("Invalid activity !!")
        var nextPosition:String? = null
        event.next?.run {
            nextPosition = activity.activityDefinition.position
        }?: run {
            activity.next?.run {
                val nextActivity = activityStepService.getOne(this)?: throw ActivityStepNotFoundException("Invalid next activity !!")
                nextPosition = nextActivity.activityDefinition.position
            }
        }


        val data = getData(event.data!!.applicationId,event.data!!.data,event.eventDefinition.type)

        return DTOEventHandleDetail(
            event.id.toString(),
            applicationId = event.data!!.applicationId.toString(),
            nextPosition = nextPosition,
            data = data.data,
            productType = data.productType,
            status = event.status,
            user = event.user
        )
    }

    private data class GetData(
        val productType:ProductType? = null,
        val data:Any?
    )

    private fun getData(applicationId:Long,data:String?,eventType: EventType):GetData{
        println(data)
        if(data != null){
            if(eventType == EventType.CHECK_CUSTOMER || eventType == EventType.CHECK_DATA|| eventType == EventType.RECOMMEND_PRODUCT){
                return GetData(
                    productType = null,
                    data = LoanApplyAssembly.convertToLoanAgent(data)
                )
            }else {
                val apply = loanApplyService.getOne(applicationId)
                apply?.run {
                    val convert:Any = when(this.productType){
                        ProductType.NEW_CLIENT -> LoanApplyAssembly.convertToNewClientLoanView(data)
                        ProductType.CLIENT -> LoanApplyAssembly.convertToClientLoanView(data)
                        ProductType.TEACHER -> LoanApplyAssembly.convertToTeacherLoanView(data)
                        ProductType.KABUHAYAN -> LoanApplyAssembly.convertToKabuhayanLoanView(data)
                        ProductType.CORPORATE -> LoanApplyAssembly.convertToCorporateLoanView(data)
                    }
                    return GetData(
                        productType = this.productType,
                        data = convert
                    )
                }
                return GetData(
                    productType = null,
                    data = null
                )
            }
        } else {
            return GetData(
                productType = null,
                data = null
            )
        }
    }

    private fun rePaged(content:MutableList<DTOEventHandleView>,pageable: Pageable):Page<DTOEventHandleView>{
        if(pageable.isUnpaged){
            return PageImpl(content)
        } else {
            val totalSize = content.size
            val start = if(pageable.pageSize * pageable.pageNumber > totalSize){
                totalSize
            } else {
                pageable.pageSize * pageable.pageNumber
            }
            val end = if(pageable.pageSize * (pageable.pageNumber + 1) > totalSize){
                totalSize
            } else {
                pageable.pageSize * (pageable.pageNumber + 1)
            }

            val newContent = content.subList(start,end)
            return PageImpl(newContent,pageable,totalSize.toLong())
        }
    }
}