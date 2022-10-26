package cn.sunline.saas.workflow.defintion.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.workflow.defintion.modules.DefinitionStatus
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(
    name = "process_definition"
)

@EntityListeners(TenantListener::class)
class ProcessDefinition (
    @Id
    val id: Long,

    @NotNull
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) not null")
    var name: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 128, columnDefinition = "varchar(128) not null")
    var status: DefinitionStatus = DefinitionStatus.NOT_START,

    @Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) default null")
    var description: String? = null,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "process_id")
    val activities: MutableList<ActivityDefinition> = mutableListOf(),

    @Temporal(TemporalType.TIMESTAMP)
    var created: Date,

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