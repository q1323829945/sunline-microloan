package cn.sunline.saas.organisation.model

import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: OrganizationInvolvement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/4/14 15:24
 */
@Entity
@Table(
    name = "organisation_involvement"
)
class OrganizationInvolvement(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name="organisation_involvement_type",nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val organizationInvolvementType: OrganizationInvolvementType,

    @NotNull
    @Column(name="party_id",nullable = false, columnDefinition = "bigint not null")
    val partyId: Long

): MultiTenant {

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