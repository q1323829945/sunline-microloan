package cn.sunline.saas.channel.arrangement.model.db

import cn.sunline.saas.global.constant.ApplyStatus
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@EntityListeners(TenantListener::class)
@Table(
    name = "channel_commission_items"
)
class ChannelCommissionItems (

    @Id
    val id: Long,

    @NotNull
    @Column(name = "apply_status", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    @Enumerated(value = EnumType.STRING)
    val applyStatus: ApplyStatus,

    @Column(name = "commission_amount",nullable = true, scale = 19, precision = 2, columnDefinition = "decimal(19,2) default null")
    val commissionAmount: BigDecimal?,

    @Column(name = "commission_ratio",nullable = true, scale = 9, precision = 6, columnDefinition = "decimal(9,6) default null")
    val commissionRatio: BigDecimal?,

    @Column(name = "commission_amount_range", nullable = true, columnDefinition = "decimal(19,2) default null")
    val commissionAmountRange: BigDecimal?,

    @Column(name = "commission_count_range", nullable = true, columnDefinition =  "bigint default null")
    val commissionCountRange: Long?,

    @NotNull
    @Column(name = "channel_arrangement_id", nullable = false, columnDefinition = "bigint not null")
    var channelArrangementId: Long,

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