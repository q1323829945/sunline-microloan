package cn.sunline.saas.channel.party.organisation.model.db

import cn.sunline.saas.channel.party.organisation.model.ChannelCastType
import cn.sunline.saas.global.constant.YesOrNo
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.util.Date
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
@EntityListeners(TenantListener::class)
class Organisation(
    @Id
    val id: Long,

    @Column(name="parent_organisation_id", columnDefinition = "bigint null")
    val parentOrganisationId: Long?,

    @NotNull
    @Column(name = "legal_entity_indicator",length = 32, columnDefinition = "varchar(32) not null")
    val legalEntityIndicator: String,

    @NotNull
    @Column(name = "organisation_sector", length = 32, columnDefinition = "varchar(32) not null")
    val organisationSector: String,

    @NotNull
    @Column(name = "organisation_registration_date")
    @Temporal(TemporalType.TIMESTAMP)
    var organisationRegistrationDate: Date?,

    @NotNull
    @Column(name = "place_of_registration",length = 128, columnDefinition = "varchar(128) not null")
    val placeOfRegistration: String,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "parent_organisation_id")
    val childOrganisation: MutableList<Organisation>? = null,

    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "id")
    var channelCast: ChannelCast? = null,

    @NotNull
    @Column(name = "enable", length = 32, columnDefinition = "varchar(32) not null")
    var enable: YesOrNo,

    ) : MultiTenant {
    @NotNull
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "organisationId")
    var businessUnits: MutableList<BusinessUnit> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL],orphanRemoval = true, mappedBy = "organisationId")
    var organizationInvolvements: MutableList<OrganizationInvolvement> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }

    @NotNull
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL],orphanRemoval = true, mappedBy = "organisationId")
    var organisationIdentifications: MutableList<OrganisationIdentification> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
        }


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