package cn.sunline.saas.interest.model.db

import cn.sunline.saas.interest.constant.InterestType
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
    name = "interest_feature",
    indexes = [Index(name = "idx_interest_feature_product_id", columnList = "product_id")]
)
class InterestFeature(
    @Id
    val id: Long,

    @NotNull
    @Column(name = "product_id", nullable = false, columnDefinition = "bigint not null")
    val productId: Long,

    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "interest_type", nullable = false, length = 32, columnDefinition = "varchar(32) not null")
    var interestType: InterestType,

    @NotNull
    @Column(name = "rate_plan_id", nullable = false, columnDefinition = "bigint not null")
    var ratePlanId: Long,

    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "id")
    var interest: InterestFeatureModality,

    @OneToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "id")
    var overdueInterest: OverdueInterestFeatureModality

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