package cn.sunline.saas.workflow.step.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.workflow.defintion.modules.db.ProcessDefinition
import cn.sunline.saas.workflow.step.modules.StepStatus
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "process_step")
@EntityListeners(TenantListener::class)
class ProcessStep(
    @Id
    val id: Long,

    @ManyToOne(optional = false)
    @JoinColumn(name="process_id", nullable=false, updatable=false)
    val processDefinition: ProcessDefinition,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var status: StepStatus = StepStatus.START,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "process_step_id")
    val activities: MutableList<ActivityStep> = mutableListOf(),

    @Temporal(TemporalType.TIMESTAMP)
    val start: Date,

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