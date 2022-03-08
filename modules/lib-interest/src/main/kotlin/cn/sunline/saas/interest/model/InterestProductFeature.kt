package cn.sunline.saas.interest.model

import cn.sunline.saas.abstract.core.banking.product.feature.ConfigurationParameter
import cn.sunline.saas.abstract.core.banking.product.feature.ProductFeature
import cn.sunline.saas.abstract.core.banking.product.feature.ProductFeatureType
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.rule.engine.model.Condition
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
    id: Long? = null,
    type: ProductFeatureType = ProductFeatureType.PRICING,
    specification: MutableList<Condition> = mutableListOf(),
    configurationOptions: MutableList<ConfigurationParameter> = mutableListOf(),
    productId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "interest_type", nullable = false, length = 16, columnDefinition = "varchar(16) not null")
    val interestType: InterestType,

    @NotNull
    @Column(name = "rate_plan_id", nullable = false, columnDefinition = "bigint not null")
    var ratePlanId: Long,

    @OneToOne
    var modality: InterestProductFeatureModality

) : ProductFeature(id, type, specification, configurationOptions, productId), MultiTenant {

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