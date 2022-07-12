package cn.sunline.saas.interest.arrangement.model.db

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.interest.constant.InterestType
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: InterestArrangement
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/21 16:27
 */
@Entity
@Table(
    name = "interest_arrangement",
)
@EntityListeners(TenantListener::class)
class InterestArrangement(

    @Id
    val id: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "interest_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    val interestType: InterestType,

    @NotNull
    @Column(nullable = false, scale = 9, precision = 6, columnDefinition = "decimal(9,6) not null")
    val rate: BigDecimal,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "base_year_days", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val baseYearDays: BaseYearDays,

    @NotNull
    @Column(name = "adjust_frequency", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    val adjustFrequency: String,

    @NotNull
    @Column(name = "overdue_interest_rate_percentage", nullable = false, columnDefinition = "decimal(9,6) not null")
    val overdueInterestRatePercentage: BigDecimal,

    @Column(name="base_rate",nullable = true, scale = 9, precision = 6, columnDefinition = "decimal(9,6) null")
    val baseRate: BigDecimal?,

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