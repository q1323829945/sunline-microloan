package cn.sunline.saas.underwriting.arrangement.model.db

import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.underwriting.arrangement.model.UnderwriterInvolvementType
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: LoanAgreementInvolvement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/25 16:23
 */
@Entity
@Table(
    name = "underwriting_arrangement_involvement"
)
@EntityListeners(TenantListener::class)
class UnderwritingArrangementInvolvement(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "party_id",  columnDefinition = "bigint not null")
    val partyId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "involvement_type", length = 64, columnDefinition = "varchar(64) not null")
    val involvementType: UnderwriterInvolvementType = UnderwriterInvolvementType.UNDERWRITER_PARTY,

    @NotNull
    @Column(name = "`primary`",  length = 1, columnDefinition = "tinyint(1) not null")
    val primary: Boolean

) : MultiTenant {

    @NotNull
    @Column(name = "tenant_id",  columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long? {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}