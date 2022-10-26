package cn.sunline.saas.workflow.step.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.workflow.defintion.modules.db.ActivityDefinition
import cn.sunline.saas.workflow.step.modules.StepStatus
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "activity_step")
@EntityListeners(TenantListener::class)
class ActivityStep (
    @Id
    val id: Long,

    @NotNull
    @Column(name = "process_step_id", nullable = false,  columnDefinition = "bigint not null")
    val processStepId: Long,

    @ManyToOne(optional = false)
    @JoinColumn(name="activity_id", nullable=false, updatable=false)
    val activityDefinition: ActivityDefinition,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var status: StepStatus = StepStatus.WAITING,

    @Column(name = "user", nullable = true, length = 128, columnDefinition = "varchar(128) default null")
    var user: String? = null,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "activity_step_id")
    val events: MutableList<EventStep> = mutableListOf(),

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