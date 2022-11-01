package cn.sunline.saas.workflow.definiton

import cn.sunline.saas.global.model.CountryType
import cn.sunline.saas.global.util.ContextUtil
import cn.sunline.saas.global.util.setTenant
import cn.sunline.saas.multi_tenant.model.Tenant
import cn.sunline.saas.multi_tenant.services.TenantService
import cn.sunline.saas.workflow.defintion.modules.DefinitionStatus
import cn.sunline.saas.workflow.defintion.modules.EventType
import cn.sunline.saas.workflow.defintion.modules.dto.DTOActivityDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOEventDefinition
import cn.sunline.saas.workflow.defintion.modules.dto.DTOProcessDefinition
import cn.sunline.saas.workflow.defintion.services.ActivityDefinitionService
import cn.sunline.saas.workflow.defintion.services.EventDefinitionService
import cn.sunline.saas.workflow.defintion.services.ProcessDefinitionService
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Pageable
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DefinitionServiceTest {
    @Autowired
    private lateinit var processDefinitionService: ProcessDefinitionService
    @Autowired
    private lateinit var activityDefinitionService: ActivityDefinitionService
    @Autowired
    private lateinit var eventDefinitionService: EventDefinitionService
    @Autowired
    private lateinit var tenantService: TenantService

    var processId1:Long? = null
    var processId2:Long? = null
    var activityId1:Long? = null
    var activityId2:Long? = null
    var activityId3:Long? = null
    var eventId1:Long? = null
    var eventId2:Long? = null



    @BeforeEach
    fun init(){
        initTenant()
        initProcessDefinition()
        initActivityDefinition()
        initEventDefinition()
    }

    @Test
    fun updateProcess(){
        val oldProcess = processDefinitionService.detail(processId1!!)
        val newProcess = processDefinitionService.updateOne(oldProcess, DTOProcessDefinition(name = "process1alter"))
        Assertions.assertThat(newProcess.status).isEqualTo(DefinitionStatus.NOT_START)
        Assertions.assertThat(newProcess.name).isEqualTo("process1alter")
    }

    @Test
    fun updateProcessStatus(){
        val newProcess = processDefinitionService.updateStatus(processId2!!,DefinitionStatus.ACTIVE)
        Assertions.assertThat(newProcess.status).isEqualTo(DefinitionStatus.ACTIVE)
    }

    @Test
    fun getActivityPaged(){
        val paged = activityDefinitionService.findPagedByProcess(processId2!!, Pageable.unpaged())
        Assertions.assertThat(paged.size).isEqualTo(2)
    }

    @Test
    fun updateActivity(){
        val activity = activityDefinitionService.updateOne(
            activityId1!!,
            DTOActivityDefinition(processId1!!, "activity1alter","test","test",2,))

        Assertions.assertThat(activity.name).isEqualTo("activity1alter")
        Assertions.assertThat(activity.position).isEqualTo("test")
        Assertions.assertThat(activity.description).isEqualTo("test")
        Assertions.assertThat(activity.sort).isEqualTo(2)
    }

    @Test
    fun deleteActivity(){
        val activity = activityDefinitionService.detail(activityId2!!)
        activityDefinitionService.remove(activity)
        val check = activityDefinitionService.getOne(activityId2!!)
        Assertions.assertThat(check).isNull()
    }

    @Test
    fun getEventPaged(){
        val paged = eventDefinitionService.findPagedByActivity(activityId1!!, Pageable.unpaged())
        Assertions.assertThat(paged.size).isEqualTo(1)
    }

    @Test
    fun updateEvent(){
        val event = eventDefinitionService.updateOne(
            eventId1!!,
            DTOEventDefinition(
                processId1!!,
                activityId1!!,
                EventType.ASSETS_ARCHIVE,
                sort = 5
            )
        )
        Assertions.assertThat(event.sort).isEqualTo(5)
    }

    @Test
    fun deleteEvent(){
        val event = eventDefinitionService.detail(eventId2!!)
        eventDefinitionService.remove(event)

        val check = eventDefinitionService.getOne(eventId2!!)
        Assertions.assertThat(check).isNull()
    }




    private fun initEventDefinition(){
        val event1 = eventDefinitionService.addOne(
            DTOEventDefinition(
                processId1!!,
                activityId1!!,
                EventType.ASSETS_ARCHIVE,
            )
        )
        eventId1 = event1.id

        val event2 = eventDefinitionService.addOne(
            DTOEventDefinition(
                processId1!!,
                activityId3!!,
                EventType.CHECK_CUSTOMER,
            )
        )
        eventId2 = event2.id
    }

    private fun initActivityDefinition(){
        val activity1 = activityDefinitionService.addOne(
            DTOActivityDefinition(
                processId = processId1!!,
                name = "activity1",
                position = null,
                description = "this is a activity definition 1",
            )
        )
        activityId1 = activity1.id

        val activity2 = activityDefinitionService.addOne(
            DTOActivityDefinition(
                processId = processId1!!,
                name = "activity2",
                position = null,
                description = "this is a activity definition 2",
            )
        )
        activityId2 = activity2.id

        val activity3 = activityDefinitionService.addOne(
            DTOActivityDefinition(
                processId = processId1!!,
                name = "activity3",
                position = null,
                description = "this is a activity definition 2",
            )
        )
        activityId3 = activity3.id

        activityDefinitionService.addOne(
            DTOActivityDefinition(
                processId = processId2!!,
                name = "activity4",
                position = null,
                description = "this is a activity definition 4",
            )
        )
        activityDefinitionService.addOne(
            DTOActivityDefinition(
                processId = processId2!!,
                name = "activity5",
                position = null,
                description = "this is a activity definition 5",
            )
        )
    }

    private fun initProcessDefinition(){
        val process1 = processDefinitionService.addOne(
            DTOProcessDefinition(
                name = "process1",
                description = "this is a process definition 1"
            )
        )
        processId1 = process1.id

        val process2 = processDefinitionService.addOne(
            DTOProcessDefinition(
                name = "process2",
                description = "this is a process definition 2"
            )
        )
        processId2 = process2.id
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
}