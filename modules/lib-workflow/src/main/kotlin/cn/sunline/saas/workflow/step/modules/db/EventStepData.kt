package cn.sunline.saas.workflow.step.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "event_data")
@EntityListeners(TenantListener::class)
class EventStepData (
    @Id
    val id: Long,

    @NotNull
    @Column(name = "applicationId", nullable = false, columnDefinition = "bigint not null")
    var applicationId:Long,

    @Column(name = "data", nullable = true, columnDefinition = "text default null")
    var data:String? = null,

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