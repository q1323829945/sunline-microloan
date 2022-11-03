package cn.sunline.saas.workflow.defintion.modules.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@Table(
    name = "activity_definition",
    indexes = [
        Index(name = "idx_activity_definition_sort", columnList = "sort"),
    ]
)
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

    @NotNull
    @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var position: String,

    @Column(nullable = true, length = 1024, columnDefinition = "varchar(1024) default null")
    var description: String? = null,

    @NotNull
    @Column(nullable = false, columnDefinition = "bigint not null")
    var sort: Long,
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

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE,CascadeType.REFRESH], orphanRemoval = true, mappedBy = "activityId")
    @OrderBy("sort asc,id desc")
    var events: MutableList<EventDefinition> = mutableListOf()
        set(value) {
            this.events.clear()
            field.addAll(value)
        }
}