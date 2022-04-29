package cn.sunline.saas.party.person.model.db

import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.party.person.model.RoleType
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "person_role")
class PersonRole(
    @Id
    val id: Long,

    @NotNull
    @Column(name="person_id", columnDefinition = "bigint not null")
    val personId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(
        length = 32,
        columnDefinition = "varchar(32) not null"
    )
    val type:RoleType

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