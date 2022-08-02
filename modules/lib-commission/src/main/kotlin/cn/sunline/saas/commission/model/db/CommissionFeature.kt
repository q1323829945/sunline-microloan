package cn.sunline.saas.commission.model.db

import cn.sunline.saas.global.constant.CommissionMethodType
import cn.sunline.saas.global.constant.CommissionType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "commission_feature")
@EntityListeners(TenantListener::class)
class CommissionFeature (
    @Id
    val id: Long,

    @NotNull
    @Column(name = "channel", nullable = false, columnDefinition = "varchar(255) not null")
    val channel: String,

    @NotNull
    @Column(name = "commission_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    @Enumerated(value = EnumType.STRING)
    val commissionType: CommissionType,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "commission_method_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val commissionMethodType: CommissionMethodType,

    @Column(name = "commission_amount",nullable = true, scale = 19, precision = 2, columnDefinition = "decimal(19,2) null")
    val commissionAmount: BigDecimal?,

    @Column(name = "commission_ratio",nullable = true, scale = 9, precision = 6, columnDefinition = "decimal(9,6) null")
    val commissionRatio: BigDecimal?,

    ) : MultiTenant {

    @NotNull
    @Column(name = "tenant_id", nullable = false, columnDefinition = "bigint not null")
    private var tenantId: Long = 0L

    override fun getTenantId(): Long? {
        return tenantId
    }

    override fun setTenantId(o: Long) {
        tenantId = o
    }
}