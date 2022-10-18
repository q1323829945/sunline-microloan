package cn.sunline.saas.interest.model.db

import cn.sunline.saas.global.constant.BaseYearDays
import cn.sunline.saas.multi_tenant.jpa.TenantListener
import cn.sunline.saas.multi_tenant.model.MultiTenant
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: InterestProductFeatureModality
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 13:35
 */
@Entity
@Table(
    name = "interest_feature_modality",
)
@EntityListeners(TenantListener::class)
class InterestFeatureModality(
    @Id
    val id: Long? = null,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "base_year_days", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var baseYearDays: BaseYearDays,

    @NotNull
    @Column(name = "adjust_frequency", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var adjustFrequency: String?,

    @Column(name = "basic_point", columnDefinition = "decimal(9,6) null")
    var basicPoint: BigDecimal?,

    @Column(name = "float_ratio", columnDefinition = "decimal(9,6) null")
    var floatRatio: BigDecimal?

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
