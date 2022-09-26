package cn.sunline.saas.channel.arrangement.model.db

import cn.sunline.saas.global.constant.*
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import java.util.Date
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
    @Enumerated(value = EnumType.STRING)
    @Column(name = "commission_method_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val commissionMethodType: CommissionMethodType,

    @Column(name = "commission_amount",nullable = true, scale = 19, precision = 2, columnDefinition = "decimal(19,2) default null")
    val commissionAmount: BigDecimal?,

    @Column(name = "commission_ratio",nullable = true, scale = 9, precision = 6, columnDefinition = "decimal(9,6) default null")
    val commissionRatio: BigDecimal?,

    @Column(name = "commission_amount_range_type", nullable = true, length = 32, columnDefinition = "varchar(32) default null")
    @Enumerated(value = EnumType.STRING)
    val commissionAmountRangeType: CommissionAmountRangeType?,

    @Column(name = "commission_count_range_type", nullable = true, length = 32, columnDefinition = "varchar(32) default null")
    @Enumerated(value = EnumType.STRING)
    val commissionCountRangeType: CommissionCountRangeType?,

    @Column(name = "lower_limit", nullable = true, columnDefinition = "decimal(19,2) default null")
    val lowerLimit: BigDecimal?,

    @Column(name = "upper_limit", nullable = true, columnDefinition = "decimal(19,2) default null")
    val upperLimit: BigDecimal?,


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