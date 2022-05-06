package cn.sunline.saas.party.person.model.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: PersonName
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 16:25
 */
@Entity
@Table(
    name = "person_name"
)
@EntityListeners(TenantListener::class)
class PersonName(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "first_name",  length = 64, columnDefinition = "varchar(64) not null")
    var firstName: String,

    @Column(name = "family_name", length = 64, columnDefinition = "varchar(64)  null")
    var familyName: String?,

    @NotNull
    @Column(name = "given_name", length = 64, columnDefinition = "varchar(64) not null")
    var givenName: String
) : MultiTenant {

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