package cn.sunline.saas.party.organisation.model

import cn.sunline.saas.multi_tenant.model.MultiTenant
import org.joda.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: Organisation
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 14:29
 */
@Entity
@Table(
    name = "organisation"
)
class Organisation(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "legal_entity_indicator", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val legalEntityIndicator: String,

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "organisation_id")
    val organisationIdentifications: MutableList<OrganisationIdentification>,

    @NotNull
    @Column(name = "organisation_sector", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val organisationSector: String,

    @NotNull
    @Column(name = "organisation_registration_date", nullable = false)
    val organisationRegistrationDate: Instant,

    @NotNull
    @Column(name = "place_of_registration", nullable = false, length = 128, columnDefinition = "varchar(128) not null")
    val placeOfRegistration: String,

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "organisation_id")
    val organizationInvolvements: MutableList<OrganizationInvolvement>,

    @NotNull
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "organisation_id")
    val businessUnits: MutableList<BusinessUnit>,

    @NotNull
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_organisation_id")
    val childOrganisation: MutableList<Organisation>

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