package cn.sunline.saas.repayment.arrangement.model.db

import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.global.constant.PrepaymentType
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: PrepaymentArrangement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/24 14:07
 */
@Entity
@Table(
    name = "prepayment_arrangement"
)
class PrepaymentArrangement(
    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val term: LoanTermType,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val type: PrepaymentType,

    @NotNull
    @Column(
        name = "penalty_ratio",
        nullable = false,
        scale = 9,
        precision = 6,
        columnDefinition = "decimal(9,6) not null"
    )
    val penaltyRatio: BigDecimal = BigDecimal.ZERO
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