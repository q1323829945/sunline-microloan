package cn.sunline.saas.party.person.model

import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
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
class PersonName(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "first_name", nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var firstName: String,

    @NotNull
    @Column(name = "family_name", nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var familyName: String,

    @NotNull
    @Column(name = "given_name", nullable = false, length = 64, columnDefinition = "varchar(64) not null")
    var givenName: String
) : MultiTenant {

    @NotNull
    @Column(name = "tenant_id", nullable = false, columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}