package cn.sunline.saas.rbac.modules

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
        name = "permission",
        indexes = [
            Index(name = "idx_permission_tag", columnList = "tag"),
            Index(name = "idx_permission_name", columnList = "name"),
        ]
)
@EntityListeners(TenantListener::class)
class Permission(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long? = null,

        @NotNull
        @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
        var tag: String,

        @NotNull
        @Column(nullable = false, length = 64, columnDefinition = "varchar(64) not null")
        var name: String,

        @Column(nullable = true, length = 255, columnDefinition = "varchar(255) not null")
        var remark: String
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