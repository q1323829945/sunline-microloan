package cn.sunline.saas.interest.model

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
    name = "interest_product_feature_modality",
)
class InterestProductFeatureModality(
    @Id
    val id: Long? = null,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "base_year_days", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var baseYearDays: BaseYearDays,

    @NotNull
    @Column(nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var adjustFrequency: String,

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
