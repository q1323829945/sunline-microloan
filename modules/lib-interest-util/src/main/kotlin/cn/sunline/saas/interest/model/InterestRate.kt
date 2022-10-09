package cn.sunline.saas.interest.model

import cn.sunline.saas.global.constant.LoanAmountTierType
import cn.sunline.saas.global.constant.LoanTermTierType
import cn.sunline.saas.global.constant.LoanTermType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: InterestRate
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 11:17
 */
@Entity
@Table(
    name = "interest_rate",
    indexes = [Index(name = "idx_interest_rate_plan_id", columnList = "rate_plan_id")]
)
@EntityListeners(TenantListener::class)
class InterestRate(
    @Id
    var id: Long? = null,

    @Enumerated(value = EnumType.STRING)
    @Column(name="from_period", nullable = true, length = 32, columnDefinition = "varchar(32) null")
    var fromPeriod: LoanTermType? = null,

    @Enumerated(value = EnumType.STRING)
    @Column(name="to_period", nullable = true, length = 32, columnDefinition = "varchar(32) null")
    var toPeriod: LoanTermType? = null,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "from_amount_period", nullable = true, length = 32, columnDefinition = "varchar(32) null")
    var fromAmountPeriod: LoanAmountTierType? = null,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "to_amount_period", nullable = true, length = 32, columnDefinition = "varchar(32) null")
    var toAmountPeriod: LoanAmountTierType? = null,

    @NotNull
    @Column(nullable = false, scale = 9, precision = 6, columnDefinition = "decimal(9,6) not null")
    var rate: BigDecimal,

    @NotNull
    @Column(name = "rate_plan_id", nullable = false, columnDefinition = "bigint not null")
    var ratePlanId: Long,

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