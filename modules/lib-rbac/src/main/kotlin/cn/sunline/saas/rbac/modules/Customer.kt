package cn.sunline.saas.rbac.modules

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "customer",
    indexes = [
        Index(name = "idx_user_id", columnList = "user_id",unique = true),
    ]
)

@EntityListeners(TenantListener::class)
class Customer (
    @Id
    var id: Long? = null,

    @Column(length = 64, columnDefinition = "varchar(64) default null")
    var username: String? = null,

    @Column(name = "user_id", updatable = false, length = 64, columnDefinition = "varchar(64) not null")
    @NotNull
    val userId: String,

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var created: Date? = null,

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    var updated: Date? = null

) : MultiTenant {

    @NotNull
    @Column(name = "tenant_id",columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }

}