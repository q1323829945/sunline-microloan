package cn.sunline.saas.channel.party.person.model.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.channel.party.person.model.PersonIdentificationType
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "person_identification", indexes = [Index(
        name = "idx_person_identification_id",
        columnList = "person_identification,person_identification_type,tenant_id",
        unique = true
    )]
)
@EntityListeners(TenantListener::class)
class PersonIdentification(
    @Id val id: Long,

    @NotNull
    @Column(name="person_id", columnDefinition = "bigint not null")
    val personId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(
        name = "person_identification_type",
        length = 32,
        columnDefinition = "varchar(32) not null"
    )
    val personIdentificationType: PersonIdentificationType,

    @NotNull
    @Column(name = "person_identification", length = 32, columnDefinition = "varchar(32) not null")
    val personIdentification: String

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