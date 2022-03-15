package cn.sunline.saas.interest.model.db

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
class InterestRate(
    @Id
    var id: Long? = null,

    @NotNull
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var period: String,

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