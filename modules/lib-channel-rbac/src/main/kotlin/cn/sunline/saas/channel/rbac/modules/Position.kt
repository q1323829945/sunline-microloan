package cn.sunline.saas.channel.rbac.modules

import cn.sunline.saas.channel.rbac.modules.User
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "position",
    indexes = [
        Index(name = "idx_position_name", columnList = "name,tenant_id", unique = true),
    ]
)
@EntityListeners(TenantListener::class)
class Position(
    @Id
    @Column(name = "id", columnDefinition = "varchar(32)")
    val id: String,

    @Column(nullable = false,length = 64, columnDefinition = "varchar(64) not null")
    @NotNull
    var name: String,


    @Column(nullable = true,columnDefinition = "text default null")
    var remark: String? = null,


    @OneToMany(fetch = FetchType.EAGER,cascade = [CascadeType.MERGE,CascadeType.REFRESH])
    @JoinColumn(name = "position_id")
    var users:MutableList<User>? = null,


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