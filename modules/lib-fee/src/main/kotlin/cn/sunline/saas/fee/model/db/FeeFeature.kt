package cn.sunline.saas.fee.model.db

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.global.constant.LoanFeeType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: FeeFeature
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/14 9:31
 */
@Entity
@Table(
    name = "fee_feature",
    indexes = [Index(name = "idx_fee_feature_product_id", columnList = "product_id")]
)
@EntityListeners(TenantListener::class)
class FeeFeature(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "product_id", nullable = false, columnDefinition = "bigint not null")
    val productId: Long,

    @NotNull
    @Column(name = "fee_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    @Enumerated(value = EnumType.STRING)
    val feeType: LoanFeeType,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "fee_method_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val feeMethodType: FeeMethodType,

    @Column(name = "fee_amount",nullable = true, scale = 19, precision = 2, columnDefinition = "decimal(19,2) null")
    val feeAmount:BigDecimal?,

    @Column(name = "fee_ratio",nullable = true, scale = 9, precision = 6, columnDefinition = "decimal(9,6) null")
    val feeRate:BigDecimal?,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "fee_deduct_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var feeDeductType: FeeDeductType

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