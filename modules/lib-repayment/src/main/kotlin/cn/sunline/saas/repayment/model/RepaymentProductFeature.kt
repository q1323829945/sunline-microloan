package cn.sunline.saas.repayment.model

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
    @Id
    val id: Long? = null,
    type: ProductFeatureType = ProductFeatureType.REPAYMENT,

    @NotNull
    @Column(name = "product_id", nullable = false, columnDefinition = "bigint not null")
    val productId: Long,

    @OneToOne
    @JoinColumn(name = "id")
    var modality: RepaymentProductFeatureModality

) : ProductFeature(),
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