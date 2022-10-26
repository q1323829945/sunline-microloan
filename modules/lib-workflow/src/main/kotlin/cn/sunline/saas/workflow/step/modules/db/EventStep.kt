package cn.sunline.saas.workflow.step.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.workflow.defintion.modules.db.EventDefinition
import cn.sunline.saas.workflow.step.modules.StepStatus
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "event_step")
@EntityListeners(TenantListener::class)
class EventStep (
    @Id
    val id: Long,

    @NotNull
    @Column(name = "activity_step_id", nullable = false, columnDefinition = "bigint not null")
    val activityStepId: Long,

    @ManyToOne(optional = false)
    @JoinColumn(name="event_id", nullable=false, updatable=false)
    val eventDefinition: EventDefinition,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var status: StepStatus = StepStatus.WAITING,

    @NotNull
    @Column(nullable = false, columnDefinition = "bigint not null")
    var sort: Long,

    @Column(name = "next", nullable = true, columnDefinition = "bigint default null")
    var next: Long? = null,

    @Temporal(TemporalType.TIMESTAMP)
    var start: Date? = null,

    @Temporal(TemporalType.TIMESTAMP)
    var end: Date? = null,

    ): MultiTenant {

    @NotNull
    @Column(name = "tenant_id", columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}