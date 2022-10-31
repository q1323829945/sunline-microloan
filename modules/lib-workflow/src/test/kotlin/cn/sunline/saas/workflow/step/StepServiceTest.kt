package cn.sunline.saas.workflow.step

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.workflow.defintion.modules.EventType
import cn.sunline.saas.workflow.defintion.modules.dto.DTOActivityDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOEventDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOProcessDefinition
import cn.sunline.saas.workflow.defintion.services.ActivityDefinitionService
import cn.sunline.saas.workflow.defintion.services.EventDefinitionService
import cn.sunline.saas.workflow.defintion.services.ProcessDefinitionService
import cn.sunline.saas.workflow.step.modules.StepStatus
import cn.sunline.saas.workflow.step.modules.dto.*
import cn.sunline.saas.workflow.step.services.ActivityStepService
import cn.sunline.saas.workflow.step.services.EventStepService
import cn.sunline.saas.workflow.step.services.ProcessStepService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StepServiceTest {
    @Autowired
    private lateinit var processDefinitionService: ProcessDefinitionService
    @Autowired
    private lateinit var activityDefinitionService: ActivityDefinitionService
    @Autowired
    private lateinit var eventDefinitionService: EventDefinitionService
    @Autowired
    private lateinit var tenantService: TenantService

    @Autowired
    private lateinit var processStepService: ProcessStepService
    @Autowired
    private lateinit var activityStepService: ActivityStepService
    @Autowired
    private lateinit var eventStepService: EventStepService

    var processDefinitionId:Long? = null
    var activityDefinitionId:Long? = null
    var eventDefinitionId:Long? = null
    var processStepId:Long? = null
    var activityStepId:Long? = null
    var eventStepId:Long? = null

    @BeforeEach
    fun init(){
        initTenant()
        initDefinition()
        initStep()
    }

    @Test
    fun getProcessStepPaged(){
        val paged = processStepService.getPaged(null,null, Pageable.unpaged())
        Assertions.assertThat(paged.content.isNotEmpty()).isEqualTo(true)
    }

    @Test
    fun updateProcessStep(){
        val process = processStepService.updateOne(
            processStepId!!,DTOProcessStepChange(
            startActivity = 123,
            status = StepStatus.PASSED,
            end = Date()
        ))

        Assertions.assertThat(process.startActivity).isEqualTo(123)
        Assertions.assertThat(process.status).isEqualTo(StepStatus.PASSED)
        Assertions.assertThat(process.end).isNotNull

    }

    @Test
    fun getActivityStepPaged(){
        val paged = activityStepService.getPaged(processStepId!!, Pageable.unpaged())
        Assertions.assertThat(paged.size).isEqualTo(1)
    }

    @Test
    fun updateActivityStep(){
        val activity = activityStepService.updateOne(activityStepId!!,
            DTOActivityStepChange(
                next = 123,
                status = StepStatus.FAILED,
                start = Date(),
                end = Date()
            )
        )

        Assertions.assertThat(activity.status).isEqualTo(StepStatus.FAILED)
        Assertions.assertThat(activity.next).isEqualTo(123)
        Assertions.assertThat(activity.start).isNotNull
        Assertions.assertThat(activity.end).isNotNull
    }

    @Test
    fun getEventStepPaged(){
        val paged = eventStepService.getPaged(activityStepId!!, Pageable.unpaged())
        Assertions.assertThat(paged.size).isEqualTo(1)
    }

    @Test
    fun updateEventStep(){
        val event = eventStepService.updateOne(eventStepId!!,
            DTOEventStepChange(
                next = 123,
                status = StepStatus.FAILED,
                start = Date(),
                end = Date()
            )
        )
        Assertions.assertThat(event.status).isEqualTo(StepStatus.FAILED)
        Assertions.assertThat(event.next).isEqualTo(123)
        Assertions.assertThat(event.start).isNotNull
        Assertions.assertThat(event.end).isNotNull
    }


    private fun initStep(){
        val processStep = processStepService.addOne(
            DTOProcessStepAdd(processDefinitionId!!)
        )
        processStepId = processStep.id

        val activityStep = activityStepService.addOne(
            DTOActivityStepAdd(
                processStep.id,
                activityDefinitionId!!,
            )
        )
        activityStepId = activityStep.id

        val eventStep = eventStepService.addOne(
            DTOEventStepAdd(
                activityStep.id,
                eventDefinitionId!!,
            )
        )
        eventStepId = eventStep.id
    }

    private fun initTenant(){
        tenantService.save(
            Tenant(
                id = 1,
                name = "test",
                country = CountryType.CHN,
                enabled = true,
                UUID.randomUUID(),
            )
        )
        ContextUtil.setTenant("1")
    }

    private fun initDefinition(){
        val processDefinition = processDefinitionService.addOne(
            DTOProcessDefinition(
                name = "process",
                description = "this is a process definition"
            )
        )
        processDefinitionId = processDefinition.id

        val activity = activityDefinitionService.addOne(
            DTOActivityDefinition(
                processId = processDefinition.id,
                name = "activity",
                position = "123123",
                description = "this is a activity definition",
            )
        )
        activityDefinitionId = activity.id

        val event = eventDefinitionService.addOne(
            DTOEventDefinition(
                processDefinition.id,
                activity.id,
                EventType.ASSETS_ARCHIVE,
            )
        )
        eventDefinitionId = event.id
    }
}