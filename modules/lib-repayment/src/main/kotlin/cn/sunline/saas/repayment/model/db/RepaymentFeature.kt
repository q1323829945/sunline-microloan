package cn.sunline.saas.repayment.model.db

import cn.sunline.saas.multi_tenant.model.MultiTenant
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
    name = "repayment_feature",
    indexes = [Index(name = "idx_repayment_feature_product_id", columnList = "product_id")]
)
class RepaymentFeature(
    @Id val id: Long,

    @NotNull
    @Column(name = "product_id", nullable = false, columnDefinition = "bigint not null")
    val productId: Long,

    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "id")
    var payment: RepaymentFeatureModality,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "repayment_id")
    var prepayment: MutableList<PrepaymentFeatureModality> = mutableListOf()

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