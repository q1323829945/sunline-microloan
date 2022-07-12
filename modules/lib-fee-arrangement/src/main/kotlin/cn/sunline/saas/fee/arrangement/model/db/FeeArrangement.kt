package cn.sunline.saas.fee.arrangement.model.db

import cn.sunline.saas.fee.constant.FeeDeductType
import cn.sunline.saas.fee.constant.FeeMethodType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: FeeArrangement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/24 16:33
 */
@Entity
@Table(
    name = "fee_arrangement",
    indexes = [Index(name="idx_fee_arrangement_agreement_id", columnList = "agreement_id")]
)
@EntityListeners(TenantListener::class)
class FeeArrangement(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "agreement_id", nullable = false, columnDefinition = "bigint not null")
    val agreementId: Long,

    @NotNull
    @Column(name = "fee_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val feeType: String,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "fee_method_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val feeMethodType: FeeMethodType,

    @Column(name = "fee_amount", nullable = true, scale = 19, precision = 2, columnDefinition = "decimal(19,2) null")
    val feeAmount: BigDecimal?,

    @Column(name = "fee_ratio", nullable = true, scale = 9, precision = 6, columnDefinition = "decimal(9,6) null")
    val feeRate: BigDecimal?,

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