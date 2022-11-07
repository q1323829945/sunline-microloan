package cn.sunline.saas.channel.arrangement.model.db

import cn.sunline.saas.global.constant.*
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull


@Entity
@EntityListeners(TenantListener::class)
@Table(
    name = "channel_arrangement"
)
class ChannelArrangement(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "channel_agreement_id", nullable = false, columnDefinition = "bigint not null")
    val channelAgreementId: Long,

    @NotNull
    @Column(name = "commission_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    @Enumerated(value = EnumType.STRING)
    val commissionType: CommissionType,

    @NotNull
    @Column(name = "channel_arrangement_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    @Enumerated(value = EnumType.STRING)
    val channelArrangementType: ChannelArrangementType = ChannelArrangementType.COMMISSION,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "commission_method_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val commissionMethodType: CommissionMethodType,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "channel_arrangement_id")
    var commissionItems: MutableList<ChannelCommissionItems> = mutableListOf()

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