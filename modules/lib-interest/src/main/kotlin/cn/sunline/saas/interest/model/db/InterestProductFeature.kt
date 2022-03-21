package cn.sunline.saas.interest.model.db

import cn.sunline.saas.abstract.core.banking.product.feature.ProductFeature
import cn.sunline.saas.abstract.core.banking.product.feature.ProductFeatureType
import cn.sunline.saas.interest.model.InterestType
import cn.sunline.saas.multi_tenant.model.MultiTenant
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: InterestProductFeature
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/7 10:46
 */
@Entity
@Table(
    name = "interest_product_feature",
    indexes = [Index(name = "idx_interest_product_feature_product_id", columnList = "product_id")]
)
class InterestProductFeature(
    @Id
    val id: Long? = null,
    type: ProductFeatureType = ProductFeatureType.PRICING,

    @NotNull
    @Column(name = "product_id", nullable = false, columnDefinition = "bigint not null")
    val productId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "interest_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    val interestType: InterestType,

    @NotNull
    @Column(name = "rate_plan_id", nullable = false, columnDefinition = "bigint not null")
    var ratePlanId: Long,

    @OneToOne
    @JoinColumn(name = "id")
    var interest: InterestProductFeatureModality,

    @OneToOne
    @JoinColumn(name = "id")
    var overdueInterest: OverdueInterestProductFeatureModality

) : ProductFeature(), MultiTenant {

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