package cn.sunline.saas.workflow.defintion.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.workflow.defintion.modules.EventType
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "event_definition")
@EntityListeners(TenantListener::class)
class EventDefinition (
    @Id
    val id: String,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    val name: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    val type: EventType,

    @Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) default null")
    val description: String? = null,

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