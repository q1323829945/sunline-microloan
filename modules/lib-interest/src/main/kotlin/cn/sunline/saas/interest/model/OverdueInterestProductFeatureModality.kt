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
    name = "overdue_interest_product_feature_modality",
)
class OverdueInterestProductFeatureModality(
    @Id
    val id: Long? = null,

    @NotNull
    @Column(name="overdue_interest_rate_percentage",nullable = false, columnDefinition = "int not null")
    var overdueInterestRatePercentage: Long,

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
