package cn.sunline.saas.person.model

import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(
    name = "person_identification", indexes = [Index(
        name = "idx_person_identification_id",
        columnList = "person_identification,person_identification_type",
        unique = true
    )]
)
class PersonIdentification(
    @Id val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(
        name = "person_identification_type",
        nullable = false,
        length = 32,
        columnDefinition = "varchar(32) not null"
    )
    val personIdentificationType: PersonIdentificationType,

    @NotNull
    @Column(name = "person_identification", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val personIdentification: String

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