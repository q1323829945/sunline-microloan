package cn.sunline.saas.party.organisation.model

import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: OrganisationIdentification
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 14:37
 */
@Entity
@Table(
    name = "organisation_identification",
    indexes = [Index(
        name = "idx_organisation_identification_id",
        columnList = "organisation_identification,organisation_identification_type",
        unique = true
    )]
)
class OrganisationIdentification(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(
        name = "organisation_identification_type",
        nullable = false,
        length = 32,
        columnDefinition = "varchar(32) not null"
    )
    val organisationIdentificationType: OrganisationIdentificationType,

    @NotNull
    @Column(
        name = "organisation_identification",
        nullable = false,
        length = 32,
        columnDefinition = "varchar(32) not null"
    )
    val organisationIdentification: String

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