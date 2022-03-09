package cn.sunline.saas.repayment.model

import cn.sunline.saas.abstract.core.banking.product.feature.ConfigurationParameter
import cn.sunline.saas.abstract.core.banking.product.feature.ProductFeature
import cn.sunline.saas.abstract.core.banking.product.feature.ProductFeatureType
import cn.sunline.saas.multi_tenant.model.MultiTenant
import cn.sunline.saas.rule.engine.model.Condition
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * @title: RepaymentProductFeature
 * @description: TODO
 * @author Kevin-Cui
 * @date 2022/3/8 15:29
 */
@Entity
@Table(
    name = "repayment_product_feature",
    indexes = [Index(name = "idx_repayment_product_feature_product_id", columnList = "product_id")]
)
class RepaymentProductFeature(
    id: Long? = null,
    type: ProductFeatureType = ProductFeatureType.PRICING,
    specification: MutableList<Condition> = mutableListOf(),
    configurationOptions: MutableList<ConfigurationParameter> = mutableListOf(),
    productId: Long,

    @OneToOne
    @JoinColumn(referencedColumnName = "id")
    var modality: RepaymentProductFeatureModality

) : ProductFeature(id, type, specification, configurationOptions, productId),
    MultiTenant {

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