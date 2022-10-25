package cn.sunline.saas.workflow.defintion.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(name = "activity_definition")
@EntityListeners(TenantListener::class)
class ActivityDefinition (
    @Id
    val id: Long,

    @NotNull
    @Column(name = "process_id", nullable = false, columnDefinition = "bigint not null")
    val processId: Long,

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var name: String,

    @Column(nullable = true, length = 64, columnDefinition = "varchar(64) default null")
    var position: String? = null,

    @Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) default null")
    var description: String? = null,

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "activity_event_definition_mapping",
        joinColumns = [JoinColumn(name = "activity_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "event_id", referencedColumnName = "id")]
    )
    var events: MutableList<EventDefinition> = mutableListOf()
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